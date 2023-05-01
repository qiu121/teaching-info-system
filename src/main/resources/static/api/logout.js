function logoutApi() {
    return axios({
        method: 'get',
        // url: 'http://192.168.3.33:8080/logout/users/'
        url: '/logout/users/'

    })
}