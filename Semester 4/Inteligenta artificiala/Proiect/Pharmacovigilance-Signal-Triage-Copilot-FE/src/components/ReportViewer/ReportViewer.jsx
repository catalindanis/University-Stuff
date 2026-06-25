import './ReportViewer.css'

export default function ReportViewer({ signal, loading }) {
  if (loading) return null;
  if (!signal) return <div className="empty-report">Selectează un semnal din stânga pentru a vedea analiza AI.</div>

  return (
    <div className="report-viewer" id="export-content">
      <div className="report-header">
        <h3>Clinical Evidence Packet</h3>
        <h2>{signal.event_title}</h2>
      </div>

      <div className="report-body">
        <section className="info-block">
          <h4>Medical Context</h4>
          <p>{signal.medical_context}</p>
        </section>

        <section className="info-block">
          <h4>Statistical Justification</h4>
          <p>{signal.statistical_justification}</p>
        </section>

        <div className="grid-2-col">
          <section className="info-block">
            <h4>Trend Analysis</h4>
            <p>{signal.trend_analysis}</p>
          </section>
          
          <section className="info-block priority-block">
            <h4>Recommended Action</h4>
            <p className="action-text">{signal.next_steps}</p>
          </section>
        </div>

        {signal.literature_references && signal.literature_references.length > 0 && (
          <section className="info-block">
            <h4>Literature References</h4>
            <ul className="ref-list">
              {signal.literature_references.map((ref, i) => (
                <li key={i}>{ref}</li>
              ))}
            </ul>
          </section>
        )}
      </div>
    </div>
  )
}