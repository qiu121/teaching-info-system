function addCollege(college) {
    return axios({
        method: 'post',
        url: '/colleges/add',
        data: college
    })
}

function removeCollege(id) {
    return axios({
        method: 'delete',
        url: `/colleges/remove/${id}`,
    })
}

function updateCollege(college) {
    return axios({
        method: 'put',
        url: '/colleges/update',
        data: college
    })
}

function listCollege() {
    return axios({
        method: 'get',
        url: '/colleges/list',
    })
}
