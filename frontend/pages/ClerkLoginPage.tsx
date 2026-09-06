import { TemplatePage } from "../TemplatePage";

const body = "\u003cmain class=\"container\"\u003e\r\n        \u003cdiv class=\"card\"\u003e\r\n            \u003cdiv class=\"card-header\"\u003e\r\n                \u003ch2\u003eContinue with Clerk\u003c/h2\u003e\r\n            \u003c/div\u003e\r\n            \u003cdiv class=\"card-body\"\u003e\r\n                \u003cp th:if=\"${#strings.isEmpty(clerkPublishableKey)}\" class=\"text-muted\"\u003e\r\n                    Clerk is not configured. Set CLERK_PUBLISHABLE_KEY to enable this sign-in method.\r\n                \u003c/p\u003e\r\n                \u003cdiv id=\"clerk-sign-in\"\u003e\u003c/div\u003e\r\n            \u003c/div\u003e\r\n        \u003c/div\u003e\r\n    \u003c/main\u003e\r\n    \u003cscript th:if=\"${!#strings.isEmpty(clerkPublishableKey)}\"\r\n            th:inline=\"javascript\"\u003e\r\n        window.addEventListener(\u0027load\u0027, async function () {\r\n            await Clerk.load({\r\n                publishableKey: /*[[${clerkPublishableKey}]]*/\r\n            });\r\n            Clerk.mountSignIn(document.getElementById(\u0027clerk-sign-in\u0027));\r\n        });\r\n    \u003c/script\u003e";
const assets: string[] = ["link:/css/register.css","script:https://cdn.jsdelivr.net/npm/@clerk/clerk-js@latest/dist/clerk.browser.js"];

export function ClerkLoginPage() {
  return <TemplatePage body={body} assets={assets} />;
}

