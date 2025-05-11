import {createRouter, createWebHistory} from 'vue-router'
import AdminLayout from "@/views/layout/AdminLayout.vue";
import FrontLayout from "@/views/layout/FrontLayout.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: getRoutes()
})

function getRoutes() {
    let defaultRoutes = [
        {
            path: '/',
            name: 'front',
            component: FrontLayout,
            redirect: '/index',
            children: [{
                path: 'index',
                name: 'index',
                component: ()=>
                    import('../views/front/Index.vue')

            },
            {
                path: 'editCurrentUser',
                name: 'editCurrentUser',
                component: ()=>
                    import('../views/EditCurrentUser.vue')
            },
            {
                path: 'editPassword',
                name: 'editPassword',
                component: ()=>
                    import('../views/EditPassword.vue')
            },
            {
                path: 'helpMessage',
                name: 'front-helpMessage',
                component: () =>
                    import('../views/front/HelpMessage.vue')
            },
            {
                path: 'helpMessageDetails/:id',
                name: 'front-helpMessageDetails',
                component: () =>
                    import('../views/front/HelpMessageDetails.vue')
            },
            {
                path:'petStore',
                name:'front-petStore',
                component: () =>
                    import("../views/front/PetStore.vue")
            },
            {
                path: "myPet",
                name: "front-myPet",
                component: () =>
                    import ('../views/front/MyPet.vue')
            },
            {
                path: "petDiary",
                name: "front-petDiary",
                component: () =>
                    import ('../views/front/PetDiary.vue')
            },
            {
                path: 'personalCenter',
                name: 'personalCenter',
                component: () =>
                    import('../views/front/PersonalCenter.vue')
            },
            {
                path: "balanceInfo",
                name: "front-balanceInfo",
                component: () =>
                    import ('../views/BalanceInfo.vue')
            },
            {
                path: 'productList',
                name: 'front-productList',
                component: () =>
                    import('../views/front/ProductList.vue')
            },
            {
                path: 'productDetails/:id',
                name: 'front-productDetails',
                component: () =>
                    import('../views/front/ProductDetails.vue')
            },

            ]
        },
        {
            path: '/admin',
            name: 'admin',
            component: AdminLayout,
            redirect: "/admin/home",
            children: [{
                path: "home",
                name: "admin-home",
                component: () =>
                    import ('../views/admin/Home.vue')
            },
                {
                    path: 'editCurrentUser',
                    name: 'admin-editCurrentUser',
                    component: () =>
                        import ('../views/EditCurrentUser.vue')
                },
                {
                    path: 'editPassword',
                    name: 'admin-editPassword',
                    component: () =>
                        import ('../views/EditPassword.vue')
                },
                {
                    path: 'admin',
                    name: 'Admin',
                    component: () =>
                        import ('../views/admin/AdminManage.vue')
                },
                {
                    path: 'user',
                    name: 'User',
                    component: () =>
                        import ('../views/admin/UserManage.vue')
                },
                {
                    path: 'petStoreManager',
                    name: 'admin-petStoreManager',
                    component: () =>
                        import ('../views/admin/PetStoreManagerManage.vue')
                },
                {
                    path: 'helpMessage',
                    name: 'admin-helpMessage',
                    component: ()=>
                        import('../views/admin/HelpMessageManage.vue')
                },
                {
                    path:'petType',
                    name:'admin-petType',
                    component: () =>
                        import("../views/admin/PetTypeManage.vue")
                },
                {
                    path: 'pet',
                    name: 'admin-pet',
                    component: () =>
                        import ('../views/admin/PetManage.vue')
                },
                {
                    path: 'petDiary',
                    name: 'admin-petDiary',
                    component: () =>
                        import ('../views/admin/PetDiaryManage.vue')
                },
                {
                    path: 'petFeed',
                    name: 'admin-petFeed',
                    component: () =>
                        import ('../views/admin/PetFeedManage.vue')
                },
                {
                    path: 'petFosterCare',
                    name: 'admin-petFosterCare',
                    component: () =>
                        import ('../views/admin/PetFosterCareManage.vue')
                },
                {
                    path: 'product',
                    name: 'admin-product',
                    component: () =>
                        import ('../views/admin/ProductManage.vue')
                },
                {
                    path: 'shippingAddress',
                    name: 'admin-shippingAddress',
                    component: () =>
                        import ('../views/admin/ShippingAddressManage.vue')
                },
                {
                    path: 'productOrder',
                    name: 'admin-productOrder',
                    component: () =>
                        import ('../views/admin/ProductOrderManage.vue')
                },

            ]
        },
        {
            path: "/login",
            name: "login",
            component: () =>
                import ('../views/Login.vue')
        }, {
            path: "/register",
            name: "register",
            component: () =>
                import ('../views/Register.vue')
        }, {
            path: "/retrievePassword",
            name: "front-retrievePassword",
            component: () =>
                import ('../views/RetrievePassword.vue')
        }];
    defaultRoutes.push({
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        meta: {
            name: ''
        },
        component: () => import ('../views/404.vue')
    })
    console.log('getDynamicRoutes', defaultRoutes)
    return defaultRoutes;
}

router.beforeEach((to, from, next) => {
    next();
});
export default router
