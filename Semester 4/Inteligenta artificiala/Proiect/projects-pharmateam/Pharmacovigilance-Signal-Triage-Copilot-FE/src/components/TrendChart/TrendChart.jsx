import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Cell } from 'recharts'
import './TrendChart.css'

export default function TrendChart({ signal }) {
  if (!signal) return null;

  const trend = signal.raw_trend || {};
  
  if (trend.latest_count === undefined || trend.baseline_average === undefined) {
    return (
      <div className="chart-empty">
        <p>Datele vizuale de trend lipsesc.</p>
        <span className="chart-hint">Vezi "Trend Analysis" în raportul de mai jos.</span>
      </div>
    );
  }

  const data = [
    { 
      name: 'Historical Baseline (Avg)', 
      cases: Number(trend.baseline_average.toFixed(2)) 
    },
    { 
      name: `Latest: ${trend.latest_month || 'Current'}`, 
      cases: trend.latest_count 
    }
  ];

  return (
    <div className="chart-container">
      <h3>Emerging Trend Detection: {signal.event_title}</h3>
      <div className="chart-wrapper">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
            <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
            <XAxis dataKey="name" stroke="#64748b" fontSize={12} tickLine={false} />
            <YAxis stroke="#64748b" fontSize={12} tickLine={false} axisLine={false} />
            <Tooltip 
              cursor={{fill: 'transparent'}}
              contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }}
            />
            <Bar dataKey="cases" radius={[6, 6, 0, 0]}>
              {data.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={index === 0 ? '#94a3b8' : 'var(--primary)'} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
      {trend.emerging && (
        <div className="trend-alert">
          ⚠️ Significant case increase detected relative to baseline!
        </div>
      )}
    </div>
  )
}