function validateEmail(el) {
    const rgx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}(\.[0-9]{1,3}){3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (rgx.test(el.value)) {
        el.classList.add('is-valid');
        el.classList.remove('is-invalid');
    } else {
        el.classList.add('is-invalid');
        el.classList.remove('is-valid');
    }
}

function validatePhone(el) {
    const rgx = /^[0-9]{11}$/;
    if (rgx.test(el.value)) {
        el.classList.add('is-valid');
        el.classList.remove('is-invalid');
    } else {
        el.classList.add('is-invalid');
        el.classList.remove('is-valid');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('keydown', event => {
            if (event.key === 'Enter') {
                event.preventDefault();
            }
        });
    }
});
