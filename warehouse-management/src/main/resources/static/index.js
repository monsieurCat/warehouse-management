/**
 * index.js
 */

const URL = 'http://localhost:8282';

let allWarehouses = [];
let curWarehouseProducts = [];
let curWarehouseId = -1;

document.addEventListener('DOMContentLoaded', () => {

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if(xhr.readyState === 4) {

          let warehouses = JSON.parse(xhr.responseText);
          warehouses.forEach(newWarehouse => {
            allWarehouses.push(newWarehouse);
            addWarehouseToSidebar(newWarehouse);
            addWarehouseToTable(newWarehouse);
          });
        }
    }

    xhr.open('GET', URL + '/warehouses');
    xhr.send();
});

function addWarehouseToSidebar(newWarehouse) {

  let li = document.createElement('li');
  let a = document.createElement('a');
  
  a.innerHTML =
  `<a href="#" class="nav-link px-0 warehouse-item" style="margin-left: 10px;"> <span id="A${newWarehouse.id}" class="d-none d-sm-inline">${newWarehouse.name}</span></a>`;

  li.setAttribute('class', 'w-100');
  li.setAttribute('id', 'LI' + newWarehouse.id);

  li.appendChild(a);
  document.getElementById('warehouse-submenu').appendChild(li);
}

function addWarehouseToTable(newWarehouse) {
    let tr = document.createElement('tr');
    let id = document.createElement('td');
    let name = document.createElement('td');
    let description = document.createElement('td');
    let currentCapacity = document.createElement('td');
    let maxCapacity = document.createElement('td'); 
    let editBtn = document.createElement('td');
    let deleteBtn = document.createElement('td');

    id.innerText = newWarehouse.id;
    name.innerText = newWarehouse.name;
    description.innerText = newWarehouse.description;
    currentCapacity.innerText = newWarehouse.currentCapacity;
    maxCapacity.innerText = newWarehouse.maxCapacity;

    editBtn.innerHTML =
    `<button class="btn btn-primary text-center update-warehouse" onclick="activateEditForm(${newWarehouse.id})" data-bs-toggle="modal" data-bs-target="#update-warehouse-modal">Edit</button>`

    deleteBtn.innerHTML =
    `<button class="btn btn-danger text-center delete-warehouse" onclick="activateDeleteForm(${newWarehouse.id})" data-bs-toggle="modal" data-bs-target="#delete-warehouse-modal">Delete</button>`

    tr.appendChild(id);
    tr.appendChild(name);
    tr.appendChild(description);
    tr.appendChild(currentCapacity);
    tr.appendChild(maxCapacity);
    tr.appendChild(editBtn);
    tr.appendChild(deleteBtn);

    tr.setAttribute('id', 'TR' + newWarehouse.id)

    document.getElementById('warehouse-table-body').appendChild(tr);
}

document.getElementById('new-warehouse-form').addEventListener('submit', (event) => {
    event.preventDefault();  

    let inputData = new FormData(document.getElementById('new-warehouse-form'));

    let newWarehouse = {
        name: inputData.get('new-warehouse-name'),
        description: inputData.get('new-warehouse-description'),
        maxCapacity: inputData.get('new-warehouse-max-capacity')
    }

    doPostRequest(newWarehouse);
    resetAllForms();
});

async function doPostRequest(newWarehouse) {
  try {
    let returnedData = await fetch(URL + "/warehouses/warehouse", {
      method : 'POST',
      headers : {
          'Content-Type' : 'application/json'
      },
      body : JSON.stringify(newWarehouse)
    });
    if (!returnedData.ok) {
      let errorMessage = await returnedData.text();
      throw new Error(errorMessage);
    }
    let warehouseJson = await returnedData.json();
    allWarehouses.push(newWarehouse);
    addWarehouseToSidebar(warehouseJson);
    addWarehouseToTable(warehouseJson);
  } catch (error) {
    displayError(error)
  } 
}

document.getElementById('update-warehouse-form').addEventListener('submit', (event) => {
    event.preventDefault();  

    let inputData = new FormData(document.getElementById('update-warehouse-form'));

    let formId = document.getElementById('update-warehouse-id').value

    const indexOfCurWarehouse = allWarehouses.findIndex(warehouse => warehouse.id == formId);

    let warehouse = {
        id: document.getElementById('update-warehouse-id').value,
        name: inputData.get('update-warehouse-name'),
        description: inputData.get('update-warehouse-description'),
        currentCapacity: allWarehouses[indexOfCurWarehouse].currentCapacity,
        maxCapacity: inputData.get('update-warehouse-max-capacity')
    }

    fetch(URL + "/warehouses/warehouse", {
        method : 'PUT',
        headers : {
            'Content-Type' : 'application/json',
        }, 
        body : JSON.stringify(warehouse)
    })
    .then((response) => {
      // if not successful
      if (!response.ok) {
        // get the error message
        return response.text().then(errorMessage => {
          // throw it so that it can display to user
          throw new Error(errorMessage)
        })
      }
      // if not failed, return response object
      return response.json();
    })
    .then((newWarehouse) => {
      // replace previous warehouse with updated warehouse
      allWarehouses.splice(indexOfCurWarehouse, 1, newWarehouse);
      updateWarehouseInTable(newWarehouse);
    })
    .catch((error) => {
      console.error(error.message);
      displayUpdateError(error)
    })
});

document.getElementById('delete-warehouse-form').addEventListener('submit', (event) => {
    event.preventDefault(); 

    let warehouseId = document.getElementById('delete-warehouse-id').value;

    let warehouse = {
        id : warehouseId,
        name : document.getElementById('delete-warehouse-name').value,
        description : document.getElementById('delete-warehouse-description').value,
        maxCapacity : document.getElementById('delete-warehouse-max-capacity').value
    }

    fetch(URL + "/warehouses/warehouse", {
        method : 'DELETE',
        headers : {
            'Content-Type' : 'application/json',
        },
        body : JSON.stringify(warehouse)
    })
    .then((data) => {
      removeWarehouseFromTable(warehouse);
    })
    .catch((error) => {
        console.error(error);
    })
});

function updateWarehouseInTable(warehouse) {
    document.getElementById('TR' + warehouse.id).innerHTML = `
    <td>${warehouse.id}</td>
    <td>${warehouse.name}</td>
    <td>${warehouse.description}</td>
    <td>${warehouse.currentCapacity}</td>
    <td>${warehouse.maxCapacity}</td>
    <td><button class="btn btn-primary text-center update-warehouse" onclick="activateEditForm(${warehouse.id})" data-bs-toggle="modal" data-bs-target="#update-warehouse-modal">Edit</button></td>
    <td><button class="btn btn-danger text-center delete-warehouse" onclick="activateDeleteForm(${warehouse.id})" data-bs-toggle="modal" data-bs-target="#delete-warehouse-modal">Delete</button></td>
    `;

    document.getElementById('A' + warehouse.id).innerText = warehouse.name;
}

function activateEditForm(warehouseId) {
    for(let w of allWarehouses) {
      if(w.id === warehouseId) {
          document.getElementById('update-warehouse-id').value = w.id;
          document.getElementById('update-warehouse-name').value = w.name;
          document.getElementById('update-warehouse-description').value = w.description;
          document.getElementById('update-warehouse-max-capacity').value = w.maxCapacity;
      }
    }
}

function activateDeleteForm(warehouseId) {
    for(let w of allWarehouses) {
        if(w.id === warehouseId) {
            document.getElementById('delete-warehouse-id').value = w.id;
            document.getElementById('delete-warehouse-name').value = w.name;
            document.getElementById('delete-warehouse-description').value = w.description;
            document.getElementById('delete-warehouse-max-capacity').value = w.maxCapacity;
        }
    }
}

function removeWarehouseFromTable(warehouse) {
    const element = document.getElementById('TR' + warehouse.id);
    const sideBarElement = document.getElementById('LI' + warehouse.id);
    element.remove();
    sideBarElement.remove();
    const indexToRemove = allWarehouses.findIndex(searchWarehouse => searchWarehouse.id == warehouse.id);
    if (indexToRemove !== -1) allWarehouses.splice(indexToRemove, 1);
}

function resetAllForms() {
    document.getElementById('new-warehouse-form').reset();
    document.getElementById('update-warehouse-form').reset();
    document.getElementById('delete-warehouse-form').reset();
}

function resetAllProductForms() {
  document.getElementById('new-product-form').reset();
  document.getElementById('update-product-form').reset();
  document.getElementById('delete-product-form').reset();
}



function displayUpdateError(errorObject) {
  errorDiv = document.getElementById('warehouse-error');

  let error = document.createElement('div');
  error.innerHTML =
  `<div class="alert alert-danger" role="alert">
  ${errorObject.message}
  </div>`;

  errorDiv.appendChild(error);

  setTimeout(() => {
    error.remove();
  }, 4000);
}

document.getElementById('home-button').addEventListener('click', (event) => {
  curWarehouseProducts = [];
  curWarehouseId = -1;

  document.getElementById('api-header').innerText = 'Warehouses';
  document.getElementById('button-container').innerHTML = '<button class="btn btn-primary text-center create-warehouse" data-bs-toggle="modal" data-bs-target="#new-warehouse-modal">Create Warehouse</button>';

  replaceTableWithWarehouses();
})

function replaceTableWithWarehouses() {
  document.getElementById('table-container').innerHTML = 
  `
  <table class="table text-center table-hover table-striped warehouse-table">
    <thead class="table-dark">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Current Capacity</th>
        <th>Max Capacity</th>
        <th>Edit</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody id="warehouse-table-body">
    </tbody>
  </table>`;

  warehousesFetchRequest();
}

async function warehousesFetchRequest() {
  try {
    let response = await fetch(URL + "/warehouses", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    });

    let warehouses = await response.json();

    allWarehouses = [];

    warehouses.forEach((warehouse) => {
      allWarehouses.push(warehouse);
      addWarehouseToTable(warehouse);
    });

  } catch (error) {
      console.error('Error fetching products:', error.message);
      displayError(error);
  }
}

// This event listener handles clicking warehouses in the sidebar
document.getElementById('warehouse-submenu').addEventListener('click', async (event) => {
  
  // If clicking on warehouse-item or any of its children
  if (event.target.matches('.warehouse-item') || event.target.closest('.warehouse-item')) {
    const dynamicElement = event.target.closest('.warehouse-item');

    // Get the name of the warehouse
    const innerSpan = dynamicElement.querySelector('span');
    const warehouseName = innerSpan.innerText;

    // Change the header to the selected warehouse's name
    document.getElementById('api-header').innerText = warehouseName;

    // adjust button to correspond with creating products
    document.getElementById('button-container').innerHTML = '<button class="btn btn-primary text-center create-product" data-bs-toggle="modal" data-bs-target="#new-product-modal">Create Product</button>';

    // Get and save some info about the selected warehouse
    await changeCurrentWarehouse(warehouseName);

    // Now replace the table with a products table and populate it
    replaceTableWithProducts(warehouseName);
  }
})

async function changeCurrentWarehouse(warehouseName) {
  let response = await fetch(URL + "/warehouses/warehouse/name/" + warehouseName, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    },
  });

  let warehouse = await response.json();
  curWarehouseProducts = [];
  curWarehouseId = warehouse.id;
}

function replaceTableWithProducts(warehouseName) {
  // Replace table
  document.getElementById('table-container').innerHTML = 
  `
  <table class="table text-center table-hover table-striped products-table">
    <thead class="table-dark">
      <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Category</th>
        <th>Size</th>
        <th>Price
        <th>Quantity</th>
        <th>Edit</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody id="product-table-body"></tbody>
  </table>`;

  productsFetchRequest(warehouseName);
}

async function productsFetchRequest(warehouseName) {
  try {
    let response = await fetch(URL + "/products/warehouse/name/" + warehouseName, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    });

    if (response.status === 204) {
      displayNoProducts();
      return;
    }

    let products = await response.json();

    curWarehouseProducts = [];

    products.forEach((product) => {
      curWarehouseProducts.push(product);
      addProductToTable(product);
    });

  } catch (error) {
      console.error('Error fetching products:', error.message);
      displayError(error);
  }
}

function addProductToTable(product) {

  let tr = document.createElement('tr');
  let name = document.createElement('td');
  let description = document.createElement('td');
  let category = document.createElement('td');
  let size = document.createElement('td');
  let price = document.createElement('td');
  let quantity = document.createElement('td'); 
  let editBtn = document.createElement('td');
  let deleteBtn = document.createElement('td');

  name.innerText = product.productName;
  description.innerText = product.description;
  category.innerText = product.categoryName;
  size.innerText = product.size;
  price.innerText = product.price;
  quantity.innerText = product.quantity;

  const underscoredProductName = product.productName.replace(/ /g, '_');

  editBtn.innerHTML =
  `<button class="btn btn-primary text-center update-product" onclick="activateProductEditForm('${underscoredProductName}')" data-bs-toggle="modal" data-bs-target="#update-product-modal">Edit</button>`

  deleteBtn.innerHTML =
  `<button class="btn btn-danger text-center delete-product" onclick="activateProductDeleteForm('${underscoredProductName}')" data-bs-toggle="modal" data-bs-target="#delete-product-modal">Delete</button>`

  tr.appendChild(name);
  tr.appendChild(description);
  tr.appendChild(category)
  tr.appendChild(size);
  tr.appendChild(price);
  tr.appendChild(quantity);
  tr.appendChild(editBtn);
  tr.appendChild(deleteBtn);

  tr.setAttribute('id', 'TR' + underscoredProductName);

  document.getElementById('product-table-body').appendChild(tr);
}

document.getElementById('new-product-form').addEventListener('submit', (event) => {
  event.preventDefault();  

  let inputData = new FormData(document.getElementById('new-product-form'));

  let newProduct = {
      productName: inputData.get('new-product-name'),
      description: inputData.get('new-product-description'),
      size: inputData.get('new-product-size'),
      categoryName: inputData.get('new-product-category'),
      price: inputData.get('new-product-price'),
      quantity: inputData.get('new-product-quantity'),
      warehouseId: curWarehouseId
  };

  indexOfExistingProduct = curWarehouseProducts.findIndex(product => product.productName == newProduct.productName);
  if (indexOfExistingProduct != -1) {
    displayProductExists();
    return;
  }

  doProductPostRequest(newProduct);
});

document.getElementById('update-product-form').addEventListener('submit', (event) => {
  event.preventDefault();  

  let inputData = new FormData(document.getElementById('update-product-form'));

  let formName = document.getElementById('update-product-name').value

  const indexOfCurProduct = curWarehouseProducts.findIndex(product => product.productName == formName);

  let newProduct = {
      productName: document.getElementById('update-product-name').value,
      description: inputData.get('update-product-description'),
      size: document.getElementById('update-product-size').value,
      categoryName: document.getElementById('update-product-category').value,
      price: inputData.get('update-product-price'),
      quantity: inputData.get('update-product-quantity'),
      warehouseId: curWarehouseId
  };

  fetch(URL + "/products/product", {
    method : 'PUT',
    headers : {
        'Content-Type' : 'application/json',
    }, 
    body : JSON.stringify(newProduct)
  })
  .then((response) => {
    if (!response.ok) {
        return response.json().then(data => {
            throw new Error(data.message || 'Failed to update product');
        });
    }
    return response.json();
  })
  .then(updatedProduct => {
    curWarehouseProducts.splice(indexOfCurProduct, 1, updatedProduct);
    updateProductInTable(updatedProduct);
    resetAllProductForms();
  })
  .catch((error) => {
    console.error(error.message);
    displayError(error);
  });
});

document.getElementById('delete-product-form').addEventListener('submit', (event) => {
  event.preventDefault();  

  let inputData = new FormData(document.getElementById('delete-product-form'));

  let newProduct = {
      productName: document.getElementById('delete-product-name').value,
      description: document.getElementById('delete-product-description').value,
      size: document.getElementById('delete-product-size').value,
      categoryName: document.getElementById('delete-product-category').value,
      price: document.getElementById('delete-product-price').value,
      quantity: document.getElementById('delete-product-quantity').value,
      warehouseId: curWarehouseId
  };

  fetch(URL + "/products/product/name/" + newProduct.productName, {
    method : 'GET',
    headers : {
      'Content-Type' : 'application/json',
    }
  }).then(response => {
    if (response.ok) {
        return response.json(); // Extract data from response
    } else {
        throw new Error('Failed to fetch product');
    }
  }).then(data => {
    // Use data from the response as the body of the second fetch request
    fetch(URL + "/products/product/warehouse/" + newProduct.warehouseId, {
      method : 'DELETE',
      headers : {
          'Content-Type' : 'application/json',
      },
      body : JSON.stringify(data) // Convert data to JSON string
    });
  }).then(() => {
    removeProductFromTable(newProduct);
    resetAllProductForms();
  }).catch(error => {
    console.error(error);
  });
});

function updateProductInTable(product) {
  const underscoredProductName = product.productName.replace(/ /g, '_');
  document.getElementById('TR' + underscoredProductName).innerHTML = `
  <td>${product.productName}</td>
  <td>${product.description}</td>
  <td>${product.categoryName}</td>
  <td>${product.size}</td>
  <td>${product.price}</td>
  <td>${product.quantity}</td>
  <td><button class="btn btn-primary text-center update-warehouse" onclick="activateProductEditForm('${underscoredProductName}')" data-bs-toggle="modal" data-bs-target="#update-product-modal">Edit</button></td>
  <td><button class="btn btn-danger text-center delete-warehouse" onclick="activateProductDeleteForm('${underscoredProductName}')" data-bs-toggle="modal" data-bs-target="#delete-product-modal">Delete</button></td>
  `;
}

function removeProductFromTable(product) {
  const underscoredProductName = product.productName.replace(/ /g, '_');
  const element = document.getElementById('TR' + underscoredProductName);
  element.remove();
  const indexToRemove = curWarehouseProducts.findIndex(searchProduct => searchProduct.productName == product.productName);
  if (indexToRemove != -1) curWarehouseProducts.splice(indexToRemove, 1);
}

function activateProductEditForm(productName) {
  const spacedProductName = productName.replace(/_/g, ' ');
  for(let p of curWarehouseProducts) {
    if(p.productName === spacedProductName) {
        document.getElementById('update-product-name').value = p.productName;
        document.getElementById('update-product-description').value = p.description;
        document.getElementById('update-product-size').value = p.size;
        document.getElementById('update-product-category').value = p.categoryName;
        document.getElementById('update-product-price').value = p.price;
        document.getElementById('update-product-quantity').value = p.quantity;
    }
  }
}

function activateProductDeleteForm(productName) {
  const spacedProductName = productName.replace(/_/g, ' ');
  for(let p of curWarehouseProducts) {
      if(p.productName === spacedProductName) {
        document.getElementById('delete-product-name').value = p.productName;
        document.getElementById('delete-product-description').value = p.description;
        document.getElementById('delete-product-size').value = p.size;
        document.getElementById('delete-product-category').value = p.categoryName;
        document.getElementById('delete-product-price').value = p.price;
        document.getElementById('delete-product-quantity').value = p.quantity;
      }
  }
}

async function doProductPostRequest(newProduct) {
  try {
      let returnedData = await fetch(URL + "/products/product", {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(newProduct)
      });

      if (!returnedData.ok) {
          let errorJson = await returnedData.json();
          throw new Error(errorJson.message || 'Failed to add product');
      }

      let productJson = await returnedData.json();

      newProduct.id = productJson.id;

      curWarehouseProducts.push(newProduct);
      addProductToTable(newProduct);
      resetAllProductForms();
  } catch (error) {
      console.error(error);
      displayError(error);
  }
}

function displayError(errorObject) {
  errorDiv = document.getElementById('warehouse-error');

  let error = document.createElement('div');
  error.classList.add('alert', 'alert-danger');
  
  if (errorObject.message) {
      // If the error message is valid JSON, display it
      error.innerHTML = `
          <div role="alert alert-danger">
              ${errorObject.message}
          </div>
      `;
  } else {
      // If the error message is not valid JSON, display the entire error object
      error.innerHTML = `
          <div role="alert alert-danger">
              ${errorObject}
          </div>
      `;
  }

  errorDiv.appendChild(error);

  setTimeout(() => {
      error.remove();
  }, 4000);
}

function displayNoProducts() {
  errorDiv = document.getElementById('warehouse-error');

  let error = document.createElement('div');
  error.innerHTML =
  `<div class="alert alert-secondary ms-100 me-100" role="alert">
  No products in warehouse.
  </div>`;

  errorDiv.appendChild(error);

  setTimeout(() => {
    error.remove();
  }, 4000);
}

function displayProductExists() {
  errorDiv = document.getElementById('warehouse-error');

  let error = document.createElement('div');
  error.innerHTML =
  `<div class="alert alert-danger ms-100 me-100" role="alert">
  Product already exists.
  </div>`;

  errorDiv.appendChild(error);

  setTimeout(() => {
    error.remove();
  }, 4000);
}

function displayWarehouseExists() {
  errorDiv = document.getElementById('warehouse-error');

  let error = document.createElement('div');
  error.innerHTML =
  `<div class="alert alert-danger ms-100 me-100" role="alert">
  Warehouse already exists.
  </div>`;

  errorDiv.appendChild(error);

  setTimeout(() => {
    error.remove();
  }, 4000);
}