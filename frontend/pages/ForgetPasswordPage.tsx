import { TemplatePage } from "../TemplatePage";

const body = "\u003cdiv class=\"container\"\u003e\r\n        \u003ch1\u003eForgot Password\u003c/h1\u003e\r\n        \u003cform th:action=\"@{/forget-password}\" method=\"post\"\u003e\r\n            \u003cdiv class=\"form-group\"\u003e\r\n                \u003clabel for=\"email\"\u003eEmail:\u003c/label\u003e\r\n                \u003cinput type=\"email\" id=\"email\" name=\"email\" required\u003e\r\n            \u003c/div\u003e\r\n            \u003cbutton type=\"submit\"\u003eReset Password\u003c/button\u003e\r\n        \u003c/form\u003e\r\n        \u003cp\u003e\u003ca th:href=\"@{/}\"\u003eBack to Home\u003c/a\u003e\u003c/p\u003e\r\n    \u003c/div\u003e";
const assets: string[] = ["link:/css/style.css"];

export function ForgetPasswordPage() {
  return <TemplatePage body={body} assets={assets} />;
}

