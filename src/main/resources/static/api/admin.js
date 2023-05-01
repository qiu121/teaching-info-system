function addUser(admin) {
    return axios({
        method: 'post',
        url: '/users/admin/add',
        data: admin
    })
}

function getUser(id) {
    return axios({
        method: 'get',
        url: `/users/admin/get/${id}`
    })
}

function removeUser(id) {
    return axios({
        method: 'delete',
        url: `/users/admin/remove/${id}`,
    })
}

function updateUser(admin) {
    return axios({
        method: 'put',
        url: '/users/admin/update',
        data: admin
    })
}

function listUser() {
    return axios({
        method: 'get',
        url: '/users/admin/list',
    })
}
