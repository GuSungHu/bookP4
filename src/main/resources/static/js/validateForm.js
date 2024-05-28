document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("registerForm").onsubmit = function() {
        const password = document.getElementById("password").value;
        const password2 = document.getElementById("password2").value;
        const passwordMismatchError = document.getElementById("passwordMismatchError");

        if (password !== password2) {
            passwordMismatchError.style.display = "block";
            return false; // Prevent form submission
        } else {
            passwordMismatchError.style.display = "none";
        }
        return true; // Allow form submission
    };
});
