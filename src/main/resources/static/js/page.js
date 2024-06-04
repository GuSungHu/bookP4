function navigate(page) {
    console.log("Navigating to page: " + page);  // Debugging line
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', page);

    const type = document.getElementById('type') ? document.getElementById('type').value : null;
    const text = document.getElementById('text') ? document.getElementById('text').value : null;

    if (type) urlParams.set('type', type);
    if (text) urlParams.set('text', text);

    console.log("URL Params: " + urlParams.toString());  // Debugging line
    window.location.href = window.location.pathname + '?' + urlParams.toString();
}
