function authUser(username) {
    return axios({
        method: 'get',
        url: '/users/permission/' + username
    })
}
