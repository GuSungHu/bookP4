// validateForm.js
function validateForm() {
    const password = document.getElementById('password').value;
    const password2 = document.getElementById('password2').value;
    const passwordMismatchError = document.getElementById('passwordMismatchError');

    if (!password || !password2) {
        passwordMismatchError.textContent = '비밀번호를 입력하세요.';
        passwordMismatchError.style.display = 'block';
        return false;
    }

    if (password !== password2) {
        passwordMismatchError.textContent = '비밀번호가 일치하지 않습니다.';
        passwordMismatchError.style.display = 'block';
        return false;
    }

    passwordMismatchError.style.display = 'none';
    return true;
}
