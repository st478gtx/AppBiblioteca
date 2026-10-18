param(
    [Parameter(Mandatory=$true)][string]$Html,
    [Parameter(Mandatory=$true)][string]$Docx,
    [Parameter(Mandatory=$true)][string]$Pdf
)
$ErrorActionPreference = 'Stop'

$word = New-Object -ComObject Word.Application
$word.Visible = $false
$word.DisplayAlerts = 0
try {
    $document = $word.Documents.Open($Html)
    try {
        $document.PageSetup.PageWidth = 612
        $document.PageSetup.PageHeight = 792
        $document.PageSetup.TopMargin = 63
        $document.PageSetup.BottomMargin = 62
        $document.PageSetup.LeftMargin = 67
        $document.PageSetup.RightMargin = 62
        $document.PageSetup.HeaderDistance = 28
        $document.PageSetup.FooterDistance = 28

        $titleRange = $document.Content
        if ($titleRange.Find.Execute('Informe integral de AppBiblioteca')) {
            $titleRange.Paragraphs.Item(1).Style = $document.Styles.Item(-63)
            $titleRange.Font.Color = 0
        }
        foreach ($picture in $document.InlineShapes) {
            # El HTML referencia los PNG por ruta local; se incrustan para que el DOCX sea portable.
            if ($null -ne $picture.LinkFormat) {
                $picture.LinkFormat.SavePictureWithDocument = $true
                $picture.LinkFormat.BreakLink()
            }
        }
        foreach ($picture in $document.InlineShapes) {
            $picture.LockAspectRatio = -1
            $picture.Width = 455
        }
        # Los enlaces file:/// solo funcionan en el equipo que generó el documento; se conserva el texto.
        for ($i = $document.Hyperlinks.Count; $i -ge 1; $i--) {
            $document.Hyperlinks.Item($i).Delete()
        }
        foreach ($table in $document.Tables) {
            $table.AutoFitBehavior(2)
            # La importación HTML agrega espaciado automático en cada celda; se compactan las filas.
            $formato = $table.Range.ParagraphFormat
            $formato.SpaceBeforeAuto = 0
            $formato.SpaceAfterAuto = 0
            $formato.SpaceBefore = 0
            $formato.SpaceAfter = 0
            $formato.LineSpacingRule = 0
            $table.Rows.HeightRule = 0
            $table.TopPadding = 3
            $table.BottomPadding = 3
            $table.Rows.Item(1).HeadingFormat = -1
        }
        # Tras compactar, las tablas de contenido ocupan todo el ancho; la tabla de portada conserva el suyo.
        for ($t = 2; $t -le $document.Tables.Count; $t++) {
            $table = $document.Tables.Item($t)
            $table.AllowAutoFit = $false
            $table.PreferredWidthType = 2
            $table.PreferredWidth = 100
        }

        $tocRange = $document.Content
        if ($tocRange.Find.Execute('INDICE_AUTOMATICO')) {
            $tocRange.Text = ''
            $toc = $document.TablesOfContents.Add($tocRange, $true, 1, 2)
            $toc.Update()
        } else {
            throw 'No se encontró el marcador de índice'
        }

        $section = $document.Sections.Item(1)
        $section.PageSetup.DifferentFirstPageHeaderFooter = $true
        $header = $section.Headers.Item(1).Range
        $header.Text = 'CIBERTEC  |  AppBiblioteca  |  Informe integral'
        $header.Font.Name = 'Aptos'
        $header.Font.Size = 8
        $header.Font.Color = 0
        $header.ParagraphFormat.Alignment = 0
        $footer = $section.Footers.Item(1).Range
        $footer.Text = "Borrador para revisi$([char]243)n grupal  |  P$([char]225)gina "
        $footer.Font.Name = 'Aptos'
        $footer.Font.Size = 8
        $footer.Font.Color = 0
        $footer.ParagraphFormat.Alignment = 2
        $footer.Collapse(0)
        $null = $document.Fields.Add($footer, 33)

        $document.Repaginate()
        $document.SaveAs2($Docx, 12)
        $document.ExportAsFixedFormat($Pdf, 17)
        Write-Output "WORD_PAGES=$($document.ComputeStatistics(2))"
        Write-Output "TABLES=$($document.Tables.Count)"
        Write-Output "PICTURES=$($document.InlineShapes.Count)"
        Write-Output "TOC=$($document.TablesOfContents.Count)"
        Write-Output "DOCX=$Docx"
        Write-Output "PDF=$Pdf"
    } finally {
        $document.Close(0)
    }
} finally {
    $word.Quit()
}
