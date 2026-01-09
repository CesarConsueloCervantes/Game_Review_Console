function mapPercentageToReviewRate(percentage) {
  if (percentage === undefined || percentage === null) return 'None';

  if (percentage >= 80) return 'Positive';
  if (percentage < 80 && percentage > 60) return 'Mostly positive';
  if (percentage <= 60 && percentage >= 40) return 'Mixed';
  if (percentage < 40 && percentage > 20) return 'Mostly Negative';
  return 'Negative';
}

module.exports = mapPercentageToReviewRate;