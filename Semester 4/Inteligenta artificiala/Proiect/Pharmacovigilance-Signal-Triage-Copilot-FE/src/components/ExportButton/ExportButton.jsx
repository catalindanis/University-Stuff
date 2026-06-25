import './../../pages/Dashboard.css'

export default function ExportButton({ filename = 'signal-packet' }) {
  function exportPrintable() {
    const el = document.getElementById('export-content')
    if (!el) return;

    const html = `
      <html>
        <head>
          <title>Signal Report - ${filename}</title>
          <style>
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 40px; color: #333; line-height: 1.6; }
            h2 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
            h3 { color: #7f8c8d; text-transform: uppercase; font-size: 14px; margin-bottom: -10px; }
            h4 { color: #2980b9; margin-top: 25px; margin-bottom: 10px; }
            .info-block { margin-bottom: 20px; background: #f9f9f9; padding: 15px; border-left: 4px solid #3498db; }
            .priority-block { border-left-color: #e74c3c; }
            .action-text { font-weight: bold; color: #c0392b; }
            ul { margin-top: 5px; }
          </style>
        </head>
        <body>
          ${el.innerHTML}
        </body>
      </html>`

    const w = window.open('', '_blank')
    if (!w) return alert('Please allow popups to export the report.');

    w.document.open()
    w.document.write(html)
    w.document.close()
    
    setTimeout(() => {
      w.focus();
      w.print();
    }, 500)
  }

  return (
    <button className="btn-export" onClick={exportPrintable}>
      <svg width="16" height="16" fill="currentColor" viewBox="0 0 16 16" style={{marginRight: '8px'}}>
        <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"/>
        <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z"/>
      </svg>
      Export PDF Report
    </button>
  )
}