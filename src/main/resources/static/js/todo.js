function showCategoryInput() {
    document.getElementById('newCategoryContainer').style.display = 'block';
    document.getElementById('category').disabled = true;
    document.getElementById('toggleCategoryButton').style.display = 'none';
}

function closeNewCategory() {
    document.getElementById('newCategoryContainer').style.display = 'none';
    document.getElementById('newCategory').value = '';
    document.getElementById('category').disabled = false;
    document.getElementById('toggleCategoryButton').style.display = 'block';
}

function addNewCategory() {
    const newCategory = document.getElementById('newCategory').value.trim();
    if (newCategory) {
        document.getElementById('newCategoryValue').value = newCategory;
    } else {
        alert("Please enter a category name.");
    }
}

function addTag() {
    const tagContainer = document.getElementById("tagsContainer");
    const newTag = document.createElement("div");
    newTag.className = "input-group mb-2 d-flex";
    newTag.innerHTML = `
        <input type="text" class="form-control" placeholder="Enter tag" name="allTags" />
        <button type="button" class="btn btn-outline-danger" onclick="removeTag(this)">
            <i class="bi bi-trash3"></i>
        </button>
    `;
    tagContainer.appendChild(newTag);
}

function removeTag(button) {
    const tagContainer = document.getElementById("tagsContainer");
    tagContainer.removeChild(button.closest(".input-group"));
}

function addAttachment() {
    const attachmentContainer = document.getElementById("attachmentsContainer");
    const newAttachment = document.createElement("div");
    newAttachment.className = "input-group mb-2 d-flex";
    newAttachment.innerHTML = `
        <input type="file" class="form-control" name="newAttachments" multiple />
        <button type="button" class="btn btn-outline-danger" onclick="removeAttachment(this)">
            <i class="bi bi-trash3"></i>
        </button>
    `;
    attachmentContainer.appendChild(newAttachment);
}

function removeAttachment(button) {
    const container = button.closest(".input-group");
    const hiddenInput = container.querySelector("input[type='hidden']");
    if (hiddenInput) {
        hiddenInput.name = "deletedAttachmentIds";
    }
    container.remove();
}
