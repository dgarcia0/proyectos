const routes = [
  {
    path: '/',
    component: () => import("layouts/PublicLayout.vue"),
    children: [
      {
        path: '',
        component: () => import("pages/public/Index.vue")
      },
      {
        path: 'registro',
        component: () => import("pages/public/Register.vue")
      },
      {
        path: 'contacto',
        component: () => import("pages/public/Contacto.vue")
      },
      {
        path: 'check',
        component: () => import("pages/public/Check.vue")
      },
    ]
  },
  {
    path: '/user',
    component: () => import("layouts/PrivateLayout.vue"),
    children: [
      {
        path: 'home',
        component: () => import("pages/private/Home.vue")
      },
      {
        path: 'playlists/:userId/:nombrePlaylist',
        component: () => import("pages/private/Playlist.vue")
      },
      {
        path: 'playlists/:userId',
        component: () => import("pages/private/Playlists.vue")
      },
      {
        path: 'descubrir',
        component: () => import("pages/private/Descubrir.vue")
      },
      {
        path: 'cancion/:cancionId',
        component: () => import("pages/private/Cancion.vue")
      },
      {
        path: 'buscador/:data',
        component: () => import("pages/private/Buscador.vue")
      },
      {
        path: 'buscador',
        component: () => import("pages/private/Buscador.vue")
      },
      {
        path: 'opciones',
        component: () => import("pages/private/Opciones.vue")
      },
      {
        path: 'perfil/:userId',
        component: () => import("pages/private/Perfil.vue")
      },
      {
        path: 'subircancion',
        component: () => import("pages/private/SubirCancion.vue")
      },
      {
        path: 'administrador',
        component: () => import("pages/private/Administrador.vue")
      },
      {
        path: 'seguidores/:userId',
        component: () => import("pages/private/Seguidores.vue")
      },
      {
        path: 'seguidos/:userId',
        component: () => import("pages/private/Seguidos.vue")
      }
    ]
  }
];

// Always leave this as last one
// if (process.env.MODE !== "ssr") {
//   routes.push({
//     path: "*",
//     component: () => import("pages/Error404.vue")
//   });
// }

export default routes;
