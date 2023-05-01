function addUser(student) {
    return axios({
        method: 'post',
        url: '/users/stu/add',
        data: student
    })
}

function updatePassword(student, oldPassword) {
    return axios({
        method: 'put',
        url: `/users/stu/update/${oldPassword}`,
        data: student
    })
}

function getUser(id) {
    return axios({
        method: 'get',
        url: `/users/stu/get/${id}`,
    })
}

function updateUser(student) {
    return axios({
        method: 'put',
        url: '/users/stu/update',
        data: student
    })
}

function removeBatchUser(idArray) {
    return axios({
        method: 'delete',
        url: `/users/stu/removeBatch/${idArray}`,
    })
}

function list(student, currentNum, pageSize) {
    return axios({
        method: 'post',
        url: `/users/stu/listAll/${currentNum}/${pageSize}`,
        data: student
    })
}
