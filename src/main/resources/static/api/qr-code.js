function generate(url) {
    return axios({
        method: 'get',
        url: '/api/v1/QRcode',
        params: url
    })
}
