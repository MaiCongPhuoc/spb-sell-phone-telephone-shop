class Product {
    constructor(id, productName, price, quantity, description, fileId, fileName, fileFolder, fileType, fileUrl) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
    }
}

    // class AppPage {
    // static drawCartItems = (obj) => {
    //     let str = `
    //             <div class="cart-item d-flex justify-content-between">
    //                 <img src="${obj.}" alt="img telephone">
    //                 <div class="cart-item-info">
    //                     <div class="cart-item-title">
    //                         <h1>${obj.productName}</h1>
    //                     </div>
    //                     <div class="cart-item-quantity">
    //                         <a href="#" id="btnMinus"><i class="fa-solid fa-angle-left"></i></a>
    //                         <input type="text" id="inputQuantity" value="1" maxlength="2" readonly>
    //                         <a href="#" id="btnPlus"><i class="fa-solid fa-angle-right"></i></a>
    //                     </div>
    //                 </div>
    //             </div>
    //         `;
    //     return str;
    // }