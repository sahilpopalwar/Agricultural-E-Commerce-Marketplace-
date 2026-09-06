import { TemplatePage } from "../TemplatePage";

const body = "\u003cdiv class=\"container\"\u003e\r\n        \u003ch2\u003eOur Products\u003c/h2\u003e\r\n        \u003cdiv class=\"products\"\u003e\r\n            \u003cdiv class=\"product\"\u003e\r\n                \u003cp\u003eProduct 1 - \u003cstrong\u003eâ‚¹10\u003c/strong\u003e\u003c/p\u003e\r\n                \u003cbutton onclick=\"addToCart(\u0027Product 1\u0027, 10)\"\u003eAdd to Cart\u003c/button\u003e\r\n            \u003c/div\u003e\r\n            \u003cdiv class=\"product\"\u003e\r\n                \u003cp\u003eProduct 2 - \u003cstrong\u003eâ‚¹15\u003c/strong\u003e\u003c/p\u003e\r\n                \u003cbutton onclick=\"addToCart(\u0027Product 2\u0027, 15)\"\u003eAdd to Cart\u003c/button\u003e\r\n            \u003c/div\u003e\r\n        \u003c/div\u003e\r\n        \u003ch2\u003eYour Shopping Cart\u003c/h2\u003e\r\n        \u003cdiv class=\"cart\"\u003e\r\n            \u003cul id=\"cart-items\"\u003e\u003c/ul\u003e\r\n            \u003cp\u003e\u003cstrong\u003eTotal: $\u003cspan id=\"total\"\u003e0\u003c/span\u003e\u003c/strong\u003e\u003c/p\u003e\r\n        \u003c/div\u003e\r\n    \u003c/div\u003e\r\n\r\n    \u003cscript src=\"/js/add_to_cart.js\"\u003e\u003c/script\u003e";
const assets: string[] = ["link:https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css","link:add_to_cart.css","script:/js/add_to_cart.js"];

export function AddToCartPage() {
  return <TemplatePage body={body} assets={assets} />;
}

