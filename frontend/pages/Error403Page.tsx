import { TemplatePage } from "../TemplatePage";

const body = "\u003cdiv class=\"error-container\"\u003e\r\n        \u003cdiv class=\"error-content\"\u003e\r\n            \u003ch1 class=\"error-code\" th:text=\"${errorCode}\"\u003e403\u003c/h1\u003e\r\n            \u003ch2 class=\"error-message\" th:text=\"${errorMessage}\"\u003eAccess Denied\u003c/h2\u003e\r\n            \u003cp\u003eYou don\u0027t have permission to access this page. Please contact the administrator if you believe this is an error.\u003c/p\u003e\r\n            \u003cdiv class=\"error-actions\"\u003e\r\n                \u003ca th:href=\"@{/}\" class=\"btn btn-primary\"\u003eGo to Homepage\u003c/a\u003e\r\n                \u003ca th:href=\"@{/contact}\" class=\"btn btn-secondary\"\u003eContact Support\u003c/a\u003e\r\n            \u003c/div\u003e\r\n        \u003c/div\u003e\r\n    \u003c/div\u003e";
const assets: string[] = ["link:@{/css/error.css}"];

export function Error403Page() {
  return <TemplatePage body={body} assets={assets} />;
}

