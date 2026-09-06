import { TemplatePage } from "../TemplatePage";

const body = "\u003cdiv class=\"error-container\"\u003e\r\n        \u003cdiv class=\"error-content\"\u003e\r\n            \u003ch1 class=\"error-code\" th:text=\"${errorCode}\"\u003e500\u003c/h1\u003e\r\n            \u003ch2 class=\"error-message\" th:text=\"${errorMessage}\"\u003eInternal Server Error\u003c/h2\u003e\r\n            \u003cp\u003eWe\u0027re sorry, but something went wrong on our end. Our team has been notified and is working to fix the issue.\u003c/p\u003e\r\n            \u003cdiv class=\"error-actions\"\u003e\r\n                \u003ca th:href=\"@{/}\" class=\"btn btn-primary\"\u003eGo to Homepage\u003c/a\u003e\r\n                \u003ca th:href=\"@{/contact}\" class=\"btn btn-secondary\"\u003eContact Support\u003c/a\u003e\r\n            \u003c/div\u003e\r\n        \u003c/div\u003e\r\n    \u003c/div\u003e";
const assets: string[] = ["link:@{/css/error.css}"];

export function Error500Page() {
  return <TemplatePage body={body} assets={assets} />;
}

