import SidebarWithHeader from "./components/shared/SideBar";
import {Text} from "@chakra-ui/react";
import React from "react";

const Home = () => {

    return (
    <SidebarWithHeader >
        <Text fontSize={"6xl"} >Dashboard</Text>
        <p>Go click out Customers tab and test out my website.</p>
    </SidebarWithHeader>

    )}

export default Home;
