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
import NavMenu from "./components/NavMenu/NavMenu.tsx";
import AuthProvider from "./components/Login/AuthProvider.tsx";
import {Toaster} from "@shadcn/components/ui/toaster.tsx";

const router = createBrowserRouter(
  createRoutesFromChildren(
    <Route element={<Layout/>}>
      <Route path="/goal" element={<GoalEditorPane/>}/>
      <Route path="/templates" element={<TemplateListPane/>}/>
      <Route path="/templates/edit" element={<TemplateEditorPane/>}/>
      <Route path="/test" element={<Test/>}/>
      <Route path="/history" element={<MessagePane/>}/>
    </Route>
  )
);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router}>
      </RouterProvider>
    </AuthProvider>
  </React.StrictMode>,
)

function Layout() {
  return <>
    <NavMenu/>
    <div className="contentRoot dark">
      <Outlet/>
      <Toaster/>
    </div>
  </>;
}