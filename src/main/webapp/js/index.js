window.addEventListener("load", async () => {
    Notiflix.Loading.pulse("Wait...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {

        await loadEmployees();
        await loadDepartments();
        await loadCurrentUser();

    } finally {
        Notiflix.Loading.remove();
    }
});

let currentPage = 1;
let rowsPerPage = 5;
let allEmployees = [];
let filteredEmployees = [];

document.getElementById("searchInput").addEventListener("keyup", () => {
    searchEmployees();
});

async function loadCurrentUser(){

    Notiflix.Loading.pulse("Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch(`api/user/current`);
        if (response.ok) {
            Notiflix.Loading.remove();

            const data = await response.json();
            console.log(data);

            const user = data.user[0];

            document.getElementById("adminTag").innerHTML =
                ` <i class="fas fa-shield-alt me-2"></i> ${user.role} -  <span id="adminRole">&nbsp; ${user.fname} ${user.lname}</span>  `;

            document.getElementById("emailSpan").innerHTML = `<i class="fas fa-database me-1"></i> ${user.email} `

            let role = user.role;

            if(role === "USER"){
                document.getElementById("addEmployeeForm").classList.add("d-none")

            }


        } else {
            Notiflix.Loading.remove();

            Notiflix.Notify.failure("Something went wrong.", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Loading.remove();

        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }finally {
        Notiflix.Loading.remove();

    }

}

async function searchEmployees(){

    const searchBy = document.getElementById("searchBy").value;
    const keyword = document.getElementById("searchInput").value;

    if(keyword.trim() === ""){
        Notiflix.Notify.warning("Enter search keyword");
        return;
    }


    try{

        const response = await fetch(
            `api/employee/search?type=${searchBy}&value=${keyword}`
        );

        if(response.ok){

            Notiflix.Loading.remove();
            const data = await response.json();
            filteredEmployees = data.employeeList || [];
            currentPage = 1;
            renderEmployees();

        }else{
            Notiflix.Notify.failure("Search failed");
        }

    }catch(e){
        Notiflix.Notify.failure(e.message);
    }finally{
        Notiflix.Loading.remove();
    }


}

async function loadDepartments(){



    try {
        const response = await fetch("api/content/loadDepartments");
        if (response.ok) {
            const data = await response.json();
            console.log(data);

            const select = document.getElementById("department");

            select.innerHTML = '<option value="">Select Department</option>';

            data.departmentList.forEach(dep =>{
                const option = document.createElement("option");
                option.value = dep.id;
                option.textContent = dep.departmentName;
                select.appendChild(option);
            });


        } else {
            Notiflix.Notify.failure("Department data loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }

}

async function loadEmployees() {

    Notiflix.Loading.pulse("Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch("api/employee/loadEmployee");
        if (response.ok) {
            Notiflix.Loading.remove();

            Notiflix.Loading.remove();
            const data = await response.json();
            allEmployees = data.employeeList || [];
            filteredEmployees = [...allEmployees];
            currentPage = 1;
            renderEmployees();


        } else {
            Notiflix.Loading.remove();

            Notiflix.Notify.failure("Employee data loading failed!", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Loading.remove();

        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }finally {
        Notiflix.Loading.remove();

    }

}


async function removeEmployee(id) {

    Notiflix.Confirm.show(
        'Confirm Delete',
        'Are you sure you want to remove this employee?',
        'Yes',
        'No',
        async function() {

            Notiflix.Loading.pulse("Removing employee...", {
                clickToClose: false,
                svgColor: '#0284c7'
            });

            try {
                const response = await fetch(`api/employee/remove/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                const data = await response.json();

                Notiflix.Loading.remove();

                if (response.ok && data.status) {
                    Notiflix.Notify.success(data.message || 'Employee removed successfully', {
                        position: 'center-top',
                        timeout: 3000
                    });

                    loadEmployees();

                } else {
                    Notiflix.Notify.failure(data.message || 'Failed to remove employee', {
                        position: 'center-top'
                    });
                }

            } catch (e) {
                Notiflix.Loading.remove();
                Notiflix.Notify.failure('Error: ' + e.message, {
                    position: 'center-top'
                });
                console.error("Remove error:", e);
            }
        }
    );
}

let viewModal;
document.addEventListener('DOMContentLoaded', function() {
    viewModal = new bootstrap.Modal(document.getElementById('viewEmployeeModal'));
});

async function viewEmployee(id){

    Notiflix.Loading.pulse("Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch(`api/content/get/${id}`);
        if (response.ok) {
            Notiflix.Loading.remove();

            const data = await response.json();
            console.log(data);

            renderViewModel(data.employee[0]);


        } else {
            Notiflix.Loading.remove();

            Notiflix.Notify.failure("Access Denied! Admin privileges required.", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Loading.remove();

        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }finally {
        Notiflix.Loading.remove();

    }

}

function renderViewModel(employee){

    document.getElementById("modalEmployeeId").innerHTML = employee.id;
    document.getElementById("modalEmployeeName").innerHTML = employee.fname + " " + employee.lname ;
    document.getElementById("modalEmployeeEmail").innerHTML = employee.email;
    document.getElementById("modalEmployeeMobile").innerHTML = employee.mobile;
    document.getElementById("modalEmployeeNic").innerHTML = employee.nic;
    document.getElementById("modalEmployeePosition").innerHTML = employee.position;
    document.getElementById("modalEmployeeDepartment").innerHTML = employee.departmentName;
    document.getElementById("modalEmployeeHireDate").innerHTML = employee.hireDate;
    document.getElementById("modalEmployeeSalary").innerHTML = employee.salary;
    document.getElementById("modalEmployeeStatus").innerHTML = employee.status == 1 ? "ACTIVE" : 'INACTIVE';

    viewModal.show();

}

async function loadEditEmployeeData(id){

    Notiflix.Loading.pulse("Loading...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    try {
        const response = await fetch(`api/content/get/${id}`);
        if (response.ok) {
            Notiflix.Loading.remove();

            const data = await response.json();
            console.log(data);

            renderEditArea(data.employee[0]);


        } else {
            Notiflix.Loading.remove();

            Notiflix.Notify.failure("Access Denied! Admin privileges required.", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Loading.remove();

        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }finally {
        Notiflix.Loading.remove();

    }

}

function renderEditArea(employee){


    document.getElementById("formTitle").innerHTML =
        ' <i class="fas fa-user-plus me-2 text-primary"></i> Edit Employee ';

    document.getElementById("addBtn").classList.add("d-none");

    const editBtn = document.getElementById("editBtn");
    editBtn.classList.remove("d-none");

    editBtn.setAttribute("data-employee-id", employee.id);


    document.getElementById("firstName").value = employee.fname;
    document.getElementById("lastName").value = employee.lname;
    document.getElementById("nic").value = employee.nic;
    document.getElementById("email").value = employee.email;
    document.getElementById("mobile").value = employee.mobile;
    document.getElementById("position").value = employee.position;
    document.getElementById("department").value = employee.departmentId;
    document.getElementById("hireDate").value = employee.hireDate;
    document.getElementById("salary").value = employee.salary;
    document.getElementById("status").value = employee.status;

}

async function EditEmployee(){

    Notiflix.Loading.pulse("Editing employee...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });

    const id = editBtn.getAttribute("data-employee-id");

    let fname = document.getElementById("firstName");
    let lname = document.getElementById("lastName");
    let email = document.getElementById("email");
    let nic = document.getElementById("nic");
    let mobile = document.getElementById("mobile");
    let position = document.getElementById("position");
    let departmentId = document.getElementById("department");
    let hireDate = document.getElementById("hireDate");
    let salary = document.getElementById("salary");
    let status = document.getElementById("status");

    const employee ={
        id: id,
        fname: fname.value,
        lname: lname.value,
        email:email.value,
        nic: nic.value,
        mobile: mobile.value,
        position: position.value,
        departmentId: departmentId.value ? Number(departmentId.value) : 0,
        hireDate: hireDate.value,
        salary: salary.value ? Number(salary.value) : 0,
        status: status.value

    }


    try{

        const response = await fetch("api/employee/edit",{
            method: "PUT",
            headers: {
                "Content-Type":"application/json"
            },
            body:JSON.stringify(employee)
        });


        if(response.ok){
            Notiflix.Loading.remove(1000)

            const data = await response.json();

            if(data.status){

                Notiflix.Loading.remove();


                Notiflix.Report.success(
                    'Employee Management System',
                    data.message,
                    'Employee edited Successfully.',
                    () => {
                        loadEmployees();
                        clearForm();
                    }
                );
            }else{
                Notiflix.Notify.failure(data.message);
            }

        }else{
            Notiflix.Notify.failure("Access Denied! Admin privileges required.", {
                position: 'center-top'
            });
        }


    }catch (e){
        Notiflix.Notify.failure(e.message);
    }finally {
        Notiflix.Loading.remove();
    }

}

async function addEmployee(){

    Notiflix.Loading.pulse("Adding employee...", {
        clickToClose: false,
        svgColor: '#0284c7'
    });


    let fname = document.getElementById("firstName");
    let lname = document.getElementById("lastName");
    let email = document.getElementById("email");
    let nic = document.getElementById("nic");
    let mobile = document.getElementById("mobile");
    let position = document.getElementById("position");
    let departmentId = document.getElementById("department");
    let hireDate = document.getElementById("hireDate");
    let salary = document.getElementById("salary");
    let status = document.getElementById("status");


    const employee ={
        fname: fname.value,
        lname: lname.value,
        email:email.value,
        nic: nic.value,
        mobile: mobile.value,
        position: position.value,
        departmentId: departmentId.value ? Number(departmentId.value) : 0,
        hireDate: hireDate.value,
        salary: salary.value ? Number(salary.value) : 0,
        status: status.value
    }

    try{

        const response = await fetch("api/employee/add",{
            method: "POST",
            headers: {
                "Content-Type":"application/json"
            },
            body:JSON.stringify(employee)
        });


        if(response.ok){
            Notiflix.Loading.remove(1000)

            const data = await response.json();

            if(data.status){

                Notiflix.Loading.remove();


                Notiflix.Report.success(
                    'Employee Management System',
                    data.message,
                    'New Employee added Successfully.',
                    () => {
                        loadEmployees();
                        clearForm();
                    }
                );
            }else{
                Notiflix.Loading.remove();
                Notiflix.Notify.failure(data.message);
            }

        }else{
            Notiflix.Loading.remove();
            Notiflix.Notify.failure("Access Denied! Admin privileges required.", {
                position: 'center-top'
            });

        }


    }catch (e){
        Notiflix.Notify.failure(e.message);
    }finally {
        Notiflix.Loading.remove();
    }



}


function renderEmployees() {
    const tbody = document.getElementById("employeeTableBody");
    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedData = filteredEmployees.slice(start, end);

    document.getElementById("startEntry").textContent = filteredEmployees.length > 0 ? start + 1 : 0;
    document.getElementById("endEntry").textContent = Math.min(end, filteredEmployees.length);
    document.getElementById("totalEntries").textContent = filteredEmployees.length;

    tbody.innerHTML = "";

    paginatedData.forEach(emp => {
        tbody.innerHTML += `<tr>
            <td class="fw-bold">${emp.id}</td>
            <td><span>${emp.fname} ${emp.lname}</span></td>
            <td>${emp.nic}</td>
            <td>${emp.position}</td>
            <td><span class="badge-department">${emp.departmentName}</span></td>
            <td>${new Date(emp.hireDate).toISOString().split("T")[0]}</td>                        
            <td>
                <span class="badge-status ${Number(emp.status) === 1 ? "badge-active" : "badge-inactive"}">
                    ${Number(emp.status) === 1 ? "A" : "I"}
                </span>
            </td>
            <td class="text-center action-icons">
                <i class="fa-regular fa-eye" title="View" onclick="viewEmployee(${emp.id})"></i>
                <i class="fa-regular fa-pen-to-square" title="Edit" onclick="loadEditEmployeeData(${emp.id})"></i>
                <i class="fa-regular fa-trash-can" title="Delete" onclick="removeEmployee(${emp.id})"></i>
            </td>
        </tr>`;
    });

    updatePaginationControls();
}

function updatePaginationControls() {
    const totalPages = Math.ceil(filteredEmployees.length / rowsPerPage);
    const pagination = document.getElementById("pagination");

    while (pagination.children.length > 2) {
        pagination.removeChild(pagination.children[1]);
    }

    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement("li");
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#" onclick="changePage(${i})">${i}</a>`;
        pagination.insertBefore(li, document.getElementById("nextPage"));
    }

    document.getElementById("prevPage").classList.toggle("disabled", currentPage === 1);
    document.getElementById("nextPage").classList.toggle("disabled", currentPage === totalPages || totalPages === 0);
}

function changePage(page) {
    const totalPages = Math.ceil(filteredEmployees.length / rowsPerPage);

    if (page === 'prev' && currentPage > 1) {
        currentPage--;
    } else if (page === 'next' && currentPage < totalPages) {
        currentPage++;
    } else if (typeof page === 'number' && page >= 1 && page <= totalPages) {
        currentPage = page;
    } else {
        return;
    }

    renderEmployees();
}

function changeRowsPerPage() {
    rowsPerPage = parseInt(document.getElementById("rowsPerPage").value);
    currentPage = 1;
    renderEmployees();
}