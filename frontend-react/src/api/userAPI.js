import axiosClient from "./axiosClient";

export const createUser = ()=>{
    return axiosClient.get('/users/');
};

export const getUserById = (id)=>{
    return axiosClient.get(`/users/${id}`);
};

export const getUserByPhone = (phone) =>{
    return axiosClient.get(`/users/phone/${phone}`);
};

export const getProfile = (id) =>{
    return axiosClient.get(`/users/${id}/profile`);
};

export const updateProfile = (hideOnline, hideLastSeen, id)=>{
    return axiosClient.put(`http://localhost:8080/users/${id}/privacy?hideOnline=${hideOnline}&hideLastSeen=${hideLastSeen}`)
}