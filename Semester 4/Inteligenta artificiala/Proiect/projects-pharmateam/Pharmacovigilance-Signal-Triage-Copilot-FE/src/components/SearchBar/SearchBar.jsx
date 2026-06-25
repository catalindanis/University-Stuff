import { useState } from 'react'
import './SearchBar.css'

export default function SearchBar({ defaultValues = {}, onSearch }) {
  const [drug, setDrug] = useState(defaultValues.drug || '')
  const [startDate, setStartDate] = useState(defaultValues.start_date || '')
  const [endDate, setEndDate] = useState(defaultValues.end_date || '')

  function submit(e) {
    e.preventDefault()
    onSearch({ drug, start_date: startDate, end_date: endDate })
  }

  return (
    <form className="searchbar-container" onSubmit={submit}>
      <div className="input-group">
        <label>Drug Name</label>
        <input 
          type="text" 
          value={drug} 
          onChange={e => setDrug(e.target.value)} 
          placeholder="e.g., Ibuprofen" 
          required 
        />
      </div>
      <div className="input-group">
        <label>Start Date</label>
        <input 
          type="date" 
          value={startDate} 
          onChange={e => setStartDate(e.target.value)} 
          required 
        />
      </div>
      <div className="input-group">
        <label>End Date</label>
        <input 
          type="date" 
          value={endDate} 
          onChange={e => setEndDate(e.target.value)} 
          required 
        />
      </div>
      <button type="submit" className="btn-search">
        Analyze
      </button>
    </form>
  )
}