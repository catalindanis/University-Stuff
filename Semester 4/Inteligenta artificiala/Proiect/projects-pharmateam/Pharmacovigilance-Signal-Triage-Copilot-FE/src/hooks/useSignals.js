import { useEffect, useState } from 'react'
import { explainSignals } from '../services/api'

export function useSignals(params) {
  const paramsKey = JSON.stringify(params)
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [selected, setSelected] = useState(null)

  useEffect(() => {
    let mounted = true
    const requestParams = JSON.parse(paramsKey)

    const fetchData = async () => {
      if (!mounted) return
      setLoading(true)
      setError(null)

      try {
        const res = await explainSignals(requestParams)
        if (!mounted) return
        
        setData(res)
        const packets = res.packets || []
        setSelected(packets[0] || null)
      } catch (err) {
        if (!mounted) return
        setError(err.message || 'Eroare la preluarea datelor.')
      } finally {
        if (mounted) setLoading(false)
      }
    }

    fetchData()

    return () => { mounted = false }
  }, [paramsKey])

  return { 
    data, 
    loading, 
    error, 
    selected, 
    selectSignal: setSelected 
  }
}