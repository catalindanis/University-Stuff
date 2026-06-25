import { useState } from 'react'
import SearchBar from '../components/SearchBar/SearchBar'
import SignalTable from '../components/SignalTable/SignalTable'
import TrendChart from '../components/TrendChart/TrendChart'
import ReportViewer from '../components/ReportViewer/ReportViewer'
import ExportButton from '../components/ExportButton/ExportButton'
import { useSignals } from '../hooks/useSignals'
import './Dashboard.css'

export default function Dashboard() {
  const [params, setParams] = useState({ 
    drug: 'Ibuprofen', 
    start_date: '2004-01-01', 
    end_date: '2024-12-31' 
  })
  
  const { data, loading, error, selectSignal, selected } = useSignals(params)

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div className="header-brand">
          <h1>PV <span>Copilot</span></h1>
          <p>AI-Powered Pharmacovigilance Triage</p>
        </div>
        <SearchBar defaultValues={params} onSearch={setParams} />
      </header>

      <main className="dashboard-layout">
        <section className="signals-panel">
          <h2>Detected Signals</h2>
          {error ? 
          <div className="alert-error">Eroare la conectarea cu API-ul. Verifică backend-ul.</div>
            :
          <SignalTable 
            signals={data?.packets || []} 
            loading={loading} 
            onSelect={selectSignal}
            selectedSignal={selected} 
          /> }
        </section>

        <section className="report-panel">
          {!loading && <TrendChart signal={selected} />}
          <ReportViewer 
            signal={selected} 
            loading={loading} 
          />
          {!loading && selected && (
            <div className="export-wrapper">
              <ExportButton filename={`signal-${selected.event.replace(/\s+/g,'_')}`} />
            </div>
          )}
        </section>
      </main>
    </div>
  )
}