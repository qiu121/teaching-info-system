//data属性,只有请求类型为GET，才可以省略？？
function addTeachInfo(teachInfo) {
    return axios({
        method: 'post',
        url: '/feedback/stu/add',
        data: teachInfo
    })
}

function list(searchForm, currentNum, pageSize) {
    return axios({
        method: 'post',
        // url: `http://192.168.3.33:8080/feedback/stu/listAll/${currentNum}/${pageSize}`,
        url: `/feedback/stu/listAll/${currentNum}/${pageSize}`,
        data: searchForm
    })
}

function listTeachInfo(username) {
    return axios({
        method: 'get',
        url: `/feedback/stu/list/${username}`,
    })
}

function getTeachInfo(id) {
    return axios({
        method: 'get',
        url: `/feedback/stu/get/${id}`,
    })
}

function removeTeachInfo(id) {
    return axios({
        method: 'delete',
        url: `/feedback/stu/remove/${id}`,
    })
}

function removeBatchTeachInfo(idArray) {
    return axios({
        method: 'delete',
        url: `/feedback/stu/removeBatch/${idArray}`,
    })
}

function updateTeachInfo(teachInfo) {
    return axios({
        method: 'put',
        url: '/feedback/stu/update/',
        data: teachInfo
    })
}
