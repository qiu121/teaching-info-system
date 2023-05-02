function addTeachInfo2(teachInfo2) {
    return axios({
        method: 'post',
        url: '/feedback/stuAdmin/add/',
        data: teachInfo2
    })
}

function removeTeachInfo2(id) {
    return axios({
        method: 'delete',
        url: `/feedback/stuAdmin/remove/${id}`,
    })
}

function getTeachInfo2(id) {
    return axios({
        method: 'get',
        url: `/feedback/stuAdmin/get/${id}`,
    })
}

function removeBatchTeachInfo2(idArray) {
    return axios({
        method: 'delete',
        url: `/feedback/stuAdmin/removeBatch/${idArray}`,
    })
}

function updateTeachInfo2(teachInfo2) {
    return axios({
        method: 'put',
        url: '/feedback/stuAdmin/update/',
        data: teachInfo2
    })
}

function listTeachInfo2(username) {
    return axios({
        method: 'get',
        url: `/feedback/stuAdmin/list/${username}`,
    })
}

function listAllTeachInfoByPermission(requestData, currentPage, pageSize) {
    const {stuAdmin, teachInfo} = requestData;
    return axios({
        method: 'post',
        url: `/feedback/stuAdmin/selectAllByPermission/${currentPage}/${pageSize}`,
        data: {stuAdmin, teachInfo}
    })
}

function listAll(teachInfo2, currentPage, pageSize) {
    return axios({
        method: 'post',
        // url: `http://192.168.3.33:8080/feedback/stuAdmin/listAll/${currentPage}/${pageSize}`,
        url: `/feedback/stuAdmin/listAll/${currentPage}/${pageSize}`,
        data: teachInfo2
    })
}
