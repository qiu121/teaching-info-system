function calculateYear() {
    const now = new Date()
    const month = now.getMonth() + 1;
    const year = now.getFullYear()

    let firstYear
    if (month >= 9) {
        firstYear = year
    } else {
        firstYear = year - 1
    }
    return [firstYear, firstYear - 1, firstYear - 2]

}
