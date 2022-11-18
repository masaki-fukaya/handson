function submitOfCreate() {
    document.forms["create"].submitWithParams({
        "title": document.getElementById("create-title").value,
        "content": document.getElementById("create-content").value,
        "tag[]": Array.from(document.getElementsByName("create-tag[]")).filter(n => n.checked).map(n => n.value),
    });
}

function submitOfUpdate(id) {
    document.forms[`update-${id}`].submitWithParams({
        "title": document.getElementById(`update-${id}-title`).value,
        "content": document.getElementById(`update-${id}-content`).value,
        "tag[]": Array.from(document.getElementsByName(`update-${id}-tag[]`)).filter(n => n.checked).map(n => n.value),
    });
}

HTMLFormElement.prototype.submitWithParams = function(params) {
    createHiddenElementForParams(params).map(h => this.appendChild(h));
    this.submit()
};

function createHiddenElementForParams(params) {
    return [].concat.apply([], Object.entries(params).map(p => {
        const name = p[0];
        const value = p[1];
        return Array.isArray(value)
            ? value.map(v => createHiddenElement(name, v))
            : createHiddenElement(name, value);
    }))
}

function createHiddenElement(name, value) {
    const input = document.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", name);
    input.setAttribute("value", value);
    return input;
}