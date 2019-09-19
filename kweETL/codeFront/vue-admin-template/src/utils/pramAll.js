export function getsession(router) {
    if (sessionStorage.getItem("name")) {
      return sessionStorage.getItem("name");

    } else {
      router.push("/login");
    }
  }