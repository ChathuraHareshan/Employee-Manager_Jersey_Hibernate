async function login(){

    Notiflix.Loading.pulse("Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    let email = document.getElementById("email");
    let password = document.getElementById("password");

    const user ={
        email: email.value,
        password: password.value
    }

    try{

        const response = await fetch("api/user/login",{
            method: "POST",
            headers: {
                "Content-Type":"application/json"
            },
            body:JSON.stringify(user)
        });

        Notiflix.Loading.pulse("wait...");

        if(response.ok){
            Notiflix.Loading.remove(1000)

            const data = await response.json();

            if(data.status){
                Notiflix.Report.success(
                    'Employee Management System',
                    data.message,
                    'Confirmation Message',
                    () => {
                        window.location.href =
                            "index.html";
                    }
                );
            }else{
                Notiflix.Notify.failure(data.message);
            }

        }else{
            Notiflix.Notify.failure('Something went wrong. Please check your credentials');
        }


    }catch (e){
        Notiflix.Notify.failure(e.message);
    }finally {
        Notiflix.Loading.remove();
    }

}

async function logout() {
    Notiflix.Confirm.show(
        'Logout Confirmation',
        'Are you sure you want to logout?',
        'Yes, Logout',
        'Cancel',
        async function() {
            Notiflix.Loading.pulse("Logging out...", {
                clickToClose: false,
                svgColor: '#0284c7'
            });

            try {
                const response = await fetch("api/user/logout", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                Notiflix.Loading.remove();

                if (response.ok) {
                    window.location.href = "login.html";
                } else {
                    const data = await response.json();
                    Notiflix.Notify.failure(data.message || "Logout failed", {
                        position: 'center-top'
                    });
                }
            } catch (e) {
                Notiflix.Loading.remove();
                Notiflix.Notify.failure('Error: ' + e.message, {
                    position: 'center-top'
                });
                console.error("Logout error:", e);
            }
        }
    );
}
