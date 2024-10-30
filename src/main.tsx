import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import './main.css'
import {GoalEditorPane} from "./components/Goals/GoalEditor.tsx";
import Test from "./components/Test.tsx";
import {createBrowserRouter, createRoutesFromChildren, Outlet, Route, RouterProvider} from "react-router-dom";
import MessagePane from "./components/ChatHistory/Pane/MessagePane.tsx";
import TemplateEditorPane from "./components/Templates/TemplateEditor/TemplateEditorPane.tsx";
import TemplateListPane from "./components/Templates/TemplateList/TemplateListPane.tsx";
import AuthProvider from "./components/Login/AuthProvider.tsx";
import {Toaster} from "@shadcn/components/ui/toaster.tsx";
import TwitchNavMenu from "./components/NavMenuTwitch/TwitchNavMenu.tsx";
import {TooltipProvider} from "@radix-ui/react-tooltip";
import HealthOverview from "./components/Health/HealthOverview.tsx";
import OauthSetup from "./components/OauthManager/OauthSetup.tsx";
import OauthResult from "./components/OauthManager/OauthResult.tsx";
import CommandsPage from "./components/Commands/CommandsPage.tsx";
import GiveawayListPage from "./components/giveaways/listPage/GiveawayListPage.tsx";

export const BOT_BACKEND_ADDR = "http://localhost:80"
export const HISTORY_BACKEND_ADDR = "http://localhost:8080"

const router = createBrowserRouter(
  createRoutesFromChildren(
    <Route element={<Layout/>}>
      <Route path="/goal" element={<GoalEditorPane/>}/>
      <Route path="/templates" element={<TemplateListPane/>}/>
      <Route path="/templates/edit" element={<TemplateEditorPane/>}/>
      <Route path="/test" element={<Test/>}/>
      <Route path="/history" element={<MessagePane/>}/>
      <Route path="*" element={<p>Diese seite gibt es nicht</p>}/>
      <Route path="/" element={<p>Diese seite gibt es nicht</p>}/>
      <Route path="/commands" element={<CommandsPage/>}/>
      <Route path="/health" element={<HealthOverview/>}/>
      <Route path="/oauth" element={<OauthSetup/>}/>
      <Route path="/oauth/result" element={<OauthResult/>}/>
      <Route path="/giveaways" element={<GiveawayListPage/>}/>
    </Route>
  )
);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <AuthProvider>
      <TooltipProvider>
      <RouterProvider router={router}>
      </RouterProvider>
      </TooltipProvider>
    </AuthProvider>
  </React.StrictMode>,
)

function Layout() {
  return <>
    <TwitchNavMenu/>
    <div className="contentRoot dark">
      <Outlet/>
      <Toaster/>
    </div>
  </>;
}