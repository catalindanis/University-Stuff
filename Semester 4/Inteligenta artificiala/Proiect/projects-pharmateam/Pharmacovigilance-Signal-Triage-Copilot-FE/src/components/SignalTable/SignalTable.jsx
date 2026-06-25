import './SignalTable.css'

export default function SignalTable({ signals = [], loading, onSelect, selectedSignal }) {
  if (loading) return (
    <div className="loading-state">
      <div className="spinner"></div>
      <p>Analizăm datele cu AI-ul local...</p>
    </div>
  )
  
  if (!signals.length) return <div className="empty-state">Nu s-au găsit semnale pentru acest interval.</div>

  const getPriorityColor = (priority) => {
    switch(priority?.toUpperCase()) {
      case 'HIGH': return 'badge-high';
      case 'MEDIUM': return 'badge-medium';
      case 'LOW': return 'badge-low';
      default: return 'badge-unknown';
    }
  }

  return (
    <div className="table-container">
      <table className="signal-table">
        <thead>
          <tr>
            <th>Adverse Event</th>
            <th>AI Priority</th>
          </tr>
        </thead>
        <tbody>
          {signals.map((s, idx) => (
            <tr 
              key={idx} 
              onClick={() => onSelect(s)}
              className={selectedSignal?.event === s.event ? 'selected-row' : ''}
            >
              <td className="event-cell">{s.event_title || s.event}</td>
              <td>
                <span className={`priority-badge ${getPriorityColor(s.priority)}`}>
                  {s.priority || 'UNKNOWN'}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}