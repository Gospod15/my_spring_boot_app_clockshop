// Тема та локалізація
const savedTheme = localStorage.getItem('shopTheme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);

function setTheme(themeName) {
    localStorage.setItem('shopTheme', themeName);
    document.documentElement.setAttribute('data-theme', themeName);
}

function setLanguage(lang) {
    localStorage.setItem('shopLang', lang);
    window.location.search = "?lang=" + lang;
}

document.addEventListener("DOMContentLoaded", function() {

    document.addEventListener('change', function(e) {
        if (e.target.classList.contains('cart-qty-input')) {
            const form = e.target.closest('form');
            if (form) {
                form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
            }
        }
    });

    function calculatePriceLocally() {
        let currentTotal = 0;
        document.querySelectorAll('.cart-item-row').forEach(row => {
            const priceText = row.querySelector('.item-price').getAttribute('data-price');
            const qtyInput = row.querySelector('.cart-qty-input');

            if (priceText && qtyInput && qtyInput.value > 0) {
                currentTotal += (parseFloat(priceText) * parseInt(qtyInput.value));
            }
        });

        const priceDisplay = document.getElementById('cartTotalDisplay');
        if (priceDisplay) {
            priceDisplay.innerText = currentTotal.toFixed(2) + " грн";
        }
    }

    document.addEventListener('input', function(e) {
        if (e.target.classList.contains('cart-qty-input')) {
            calculatePriceLocally();
        }
    });

    document.body.addEventListener('submit', function(e) {
        const form = e.target;

        if (form.matches('.add-to-cart-form, .update-cart-form, .remove-cart-form, .clear-cart-form')) {
            e.preventDefault(); // Блокуємо перезавантаження сторінки

            const submitBtn = form.querySelector('button[type="submit"]');
            const originalBtnHtml = submitBtn ? submitBtn.innerHTML : '';

            if (form.classList.contains('remove-cart-form')) {
                form.closest('.cart-item-row').remove();
                calculatePriceLocally();
            }

            if (form.classList.contains('clear-cart-form')) {
                const tbody = document.querySelector('.table-responsive tbody');
                if(tbody) tbody.innerHTML = '';
                calculatePriceLocally();
            }

            if (form.classList.contains('add-to-cart-form')) {
                if (submitBtn) {
                    submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i>';
                    submitBtn.disabled = true;
                }
                const badge = document.getElementById('cartBadge');
                if (badge) {
                    badge.style.display = 'inline-block';
                    const qtyInput = form.querySelector('input[name="quantity"]');
                    const qty = qtyInput ? parseInt(qtyInput.value) : 1;
                    const currentCount = parseInt(badge.innerText || 0);
                    badge.innerText = currentCount + qty;
                }
            }

            fetch(form.action, {
                method: 'POST',
                body: new FormData(form)
            })
                .then(response => response.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');

                    const newModalBody = doc.getElementById('cartModalBody');
                    const oldModalBody = document.getElementById('cartModalBody');
                    if (newModalBody && oldModalBody) {
                        oldModalBody.innerHTML = newModalBody.innerHTML;
                    }

                    const newBadge = doc.getElementById('cartBadge');
                    const currentBadge = document.getElementById('cartBadge');
                    if (newBadge && currentBadge) {
                        currentBadge.innerText = newBadge.innerText;
                        currentBadge.style.display = newBadge.style.display;
                    }

                    if (form.classList.contains('add-to-cart-form') && submitBtn) {
                        submitBtn.innerHTML = '<i class="bi bi-check2"></i>';
                        submitBtn.classList.replace('btn-dark', 'btn-success');
                        setTimeout(() => {
                            submitBtn.innerHTML = originalBtnHtml;
                            submitBtn.classList.replace('btn-success', 'btn-dark');
                            submitBtn.disabled = false;
                        }, 800);
                    }
                })
                .catch(error => {
                    console.error("AJAX Error:", error);
                    if (submitBtn) {
                        submitBtn.innerHTML = originalBtnHtml;
                        submitBtn.disabled = false;
                    }
                });
        }
    });
});