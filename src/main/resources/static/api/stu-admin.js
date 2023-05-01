function addUser(stuAdmin) {
    return axios({
        method: 'post',
        url: '/users/stuAdmin/add',
        data: stuAdmin
    })
}

function removeBatchUser(idArray) {
    return axios({
        method: 'delete',
        url: `/users/stuAdmin/removeBatch/${idArray}`,
    })
}

function getUser(id) {
    return axios({
        method: 'get',
        url: `/users/stuAdmin/get/${id}`,
    })
}

function updateUser(stuAdmin) {
    return axios({
        method: 'put',
        url: '/users/stuAdmin/update/',
        data: stuAdmin
    })
}

function list(stuAdmin, currentNum, pageSize) {
    return axios({
        method: 'post',
        url: `/users/stuAdmin/list/${currentNum}/${pageSize}`,
        data: stuAdmin
    })
}
