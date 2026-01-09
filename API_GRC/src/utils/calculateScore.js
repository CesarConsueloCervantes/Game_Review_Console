function calculateScore(positive, negative, mixed, k = 0.08) {
  const totalPN = positive + negative;

  // Si no hay positivos ni negativos, el resultado es neutral 0.5
  if (totalPN === 0) return 0.5;

  const base = positive / totalPN;
  
  // Aplicar amortiguador con mixed
  const damping = 1 - Math.exp(-k * mixed);
  
  const score = base - (base - 0.5) * damping;

  // Limitar entre 0 y 1 por seguridad numérica
  return Math.min(1, Math.max(0, score));
}

module.exports = calculateScore;