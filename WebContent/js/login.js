function nameEntered(){
    var value = document.getElementById('name').value;
    if(value === ""){
        document.getElementById('nameMessage').innerHTML = "Polje ne smije ostati prazno";
    }else{
        document.getElementById('nameMessage').innerHTML = "";
    }
}

function surnameEntered(){
    var value = document.getElementById('surname').value;
    if(value === ""){
        document.getElementById('surnameMessage').innerHTML = "Polje ne smije ostati prazno";
    }else{
        document.getElementById('surnameMessage').innerHTML = "";
    }
}

function usernameEntered(){
    var value = document.getElementById('username').value;
    if(value === ""){
        document.getElementById('usernameMessage').innerHTML = "Polje ne smije ostati prazno";
    }else
    document.getElementById('usernameMessage').innerHTML = "";
}

function mailEntered(){
    var value = document.getElementById('mail').value;
    if(value === ""){
        document.getElementById('mailMessage').innerHTML = "Polje ne smije ostati prazno";
    }else{
        document.getElementById('mailMessage').innerHTML = "";
    }
}

function passwordEntered(){
    var value = document.getElementById('password').value;
    if(value === ""){
        document.getElementById('passwordMessage').innerHTML = "Polje ne smije ostati prazno";
    }else{
        document.getElementById('passwordMessage').innerHTML = "";
    }
}

function rePasswordEntered(){
    var value = document.getElementById('rePassword').value;
    if(value === ""){
        document.getElementById('rePasswordMessage').innerHTML = "Polje ne smije ostati prazno";
    }else if(document.getElementById('password').value !== value){
        document.getElementById('rePasswordMessage').innerHTML = "Lozinke moraju biti iste";
    }else{
        document.getElementById('rePasswordMessage').innerHTML = "";
    }
}

function submitAccountForm(){
    document.getElementById('accountForm').submit();
}

function submitLogoutForm(){
    document.getElementById('logoutForm').submit();
}