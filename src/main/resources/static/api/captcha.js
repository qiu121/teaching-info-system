function generate(randomCode) {
    return axios({
        method: 'get',
        url: `/api/v1/captcha/${randomCode}`,
    })
}
