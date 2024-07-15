
import {
    Button,
    Flex,
    FormLabel,
    Heading,
    Input,
    Link,
    Stack,
    Image,
    Text,
    Box,
    Alert,
    AlertIcon,
    Center,
  } from "@chakra-ui/react";
  import {useAuth} from "../context/AuthContext.jsx";
  import {useNavigate} from "react-router-dom";
  import {useEffect} from "react";
  import CreateCustomerForm from "../shared/CreateCustomerForm";


  const Signup = () =>{
        const {customer, setCustomerFromToken }  = useAuth();
        const navigate = useNavigate();
        useEffect((customer)=>{
          if(customer){
            navigate("/dashboard")
          }
        })
      
        return (
          <Stack minH={"100vh"} direction={{ base: "column", md: "row" }}>
            <Flex p={8} flex={1} align={"center"} justifyContent={"center"}>
              <Stack spacing={4} w={"full"} maxW={"md"}>
                <Image
                  src={"src/assets/amigoscode_logo.jpeg"}
                  boxSize={"200px"}
                  alt={"Logo"}
                />
                <Heading fontSize={"2xl"} mb={15}>
                  Register a new account
                </Heading>
                <CreateCustomerForm onSuccess={(token)=>{
                    localStorage.setItem("access_token", token)
                    setCustomerFromToken()
                    navigate("/dashboard")
                }}/>
                <Link color={"blue.500"} href="/">
                  Have an account? Sign in
                </Link>
              </Stack>
            </Flex>
            <Flex
              flex={1}
              p={10}
              flexDirection={"column"}
              alignItems={"center"}
              justifyContent={"center"}
              bgGradient={{ sm: "linear(to-r, blue.600, purple.600)" }}
            >
              <Text fontSize={"6xl"} color={"white"} fontWeight={"bold"} mb={5}>
                <Link target={"_blank"} href={"https://amigoscode.com/courses"}>
                  Enrol Now
                </Link>
              </Text>
      
              <Image
                alt={"Login Image"}
                objectFit={"scale-down"}
                src={
                  "https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png"
                }
              />
            </Flex>
          </Stack>
        );
      }


export default Signup;