// Verificación local con Chrome DevTools Protocol; sin paquetes externos.
import { spawn } from 'node:child_process';
import { mkdtempSync, readFileSync, existsSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join, resolve, sep } from 'node:path';
import { pathToFileURL } from 'node:url';

const chrome = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe';
const profile = mkdtempSync(join(tmpdir(), 'biblioteca-cdp-'));
const page = resolve('docs/explorador/index.html');
const child = spawn(chrome, ['--headless=new', '--disable-gpu', '--no-first-run',
  '--remote-debugging-port=0', `--user-data-dir=${profile}`, pathToFileURL(page).href],
  { windowsHide: true, stdio: 'ignore' });
const sleep = ms => new Promise(r => setTimeout(r, ms));
let socket, nextId = 1;
const pending = new Map(), errors = [];
function send(method, params = {}) {
  const id = nextId++;
  return new Promise((resolve, reject) => { pending.set(id, { resolve, reject }); socket.send(JSON.stringify({ id, method, params })); });
}
async function evalJs(expression) {
  const result = await send('Runtime.evaluate', { expression, returnByValue: true, awaitPromise: true });
  if (result.error || result.result?.exceptionDetails) throw new Error(JSON.stringify(result.error || result.result.exceptionDetails));
  return result.result.result?.value;
}
function ensure(condition, message) { if (!condition) throw new Error(message); }
try {
  let port;
  for (let i = 0; i < 100; i++) {
    const file = join(profile, 'DevToolsActivePort');
    if (existsSync(file)) { port = readFileSync(file, 'utf8').split('\n')[0]; break; }
    if (child.exitCode !== null) throw new Error(`Chrome terminó: ${child.exitCode}`);
    await sleep(100);
  }
  ensure(port, 'Chrome no abrió CDP');
  const pages = await (await fetch(`http://127.0.0.1:${port}/json/list`)).json();
  const target = pages.find(x => x.type === 'page' && x.url.startsWith('file:'));
  ensure(target, 'No se abrió index.html local');
  socket = new WebSocket(target.webSocketDebuggerUrl);
  await new Promise((resolve, reject) => { socket.onopen = resolve; socket.onerror = reject; });
  socket.onmessage = event => {
    const message = JSON.parse(event.data);
    if (message.id && pending.has(message.id)) {
      const p = pending.get(message.id); pending.delete(message.id);
      if (message.error) p.reject(new Error(message.error.message)); else p.resolve(message);
    }
    if (message.method === 'Runtime.exceptionThrown') errors.push(message.params.exceptionDetails.text);
    if (message.method === 'Runtime.consoleAPICalled' && message.params.type === 'error') errors.push('console.error');
  };
  await send('Runtime.enable');
  for (let i = 0; i < 40; i++) {
    if (await evalJs("document.querySelector('h1.title')?.textContent")) break;
    await sleep(100);
  }
  ensure((await evalJs("document.querySelector('h1.title')?.textContent")) === 'Arquitectura general', 'La vista inicial no se renderizó');
  const flowIds = ['arquitectura','arranque','usuarios','categorias','autores','libros','prestamos','detalle','boletas','persistencia'];
  for (const id of flowIds) {
    await evalJs(`document.querySelector('[data-id="${id}"]').click()`);
    await evalJs("document.querySelector('[data-tab=\"Técnica\"]').click()");
    const diagrams = await evalJs("[...document.querySelectorAll('.canvas svg')].map(s=>s.children.length)");
    ensure(diagrams.length === 2 && diagrams.every(x => x > 3), `Diagramas vacíos: ${id}`);
    await evalJs("document.querySelector('[data-tab=\"Código\"]').click()");
    ensure(await evalJs("document.querySelectorAll('.method').length") > 0, `Código vacío: ${id}`);
  }
  await evalJs("document.querySelector('[data-id=\"autores\"]').click()");
  await evalJs("document.querySelector('[data-tab=\"Técnica\"]').click()");
  const before = await evalJs("document.querySelector('#diag-seq').style.width");
  await evalJs("document.querySelector('[data-zoom=\"diag-seq\"][data-d=\"1\"]').click()");
  ensure(before !== await evalJs("document.querySelector('#diag-seq').style.width"), 'El zoom no cambió');
  await evalJs("document.querySelector('[data-source=\"diag-seq\"]').click()");
  ensure(await evalJs("document.querySelector('#diag-seq-source').classList.contains('show')"), 'No se mostró Mermaid');
  await evalJs("let q=document.querySelector('#search');q.value='PrestamoService';q.dispatchEvent(new Event('input',{bubbles:true}))");
  ensure(await evalJs("document.querySelectorAll('#nav button').length") >= 2, 'El buscador no encontró métodos');
  ensure(errors.length === 0, `Errores JavaScript: ${errors.join(', ')}`);
  console.log('OK Chrome file://: 10 flujos, navegación, pestañas, búsqueda, código, 20 diagramas, zoom, Mermaid y consola sin errores');
} finally {
  if (socket) socket.close();
  child.kill();
  if (child.exitCode === null) await Promise.race([new Promise(r => child.once('exit', r)), sleep(3000)]);
  await sleep(600);
  const root = resolve(tmpdir()) + sep;
  if (!resolve(profile).startsWith(root)) throw new Error('Perfil temporal fuera de TEMP');
  rmSync(profile, { recursive: true, force: true, maxRetries: 20, retryDelay: 200 });
}
