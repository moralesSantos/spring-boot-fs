import {
    Button,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    useDisclosure,
    Stack,
    flexbox
  } from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm";
  
  const AddIcon = () => "+";
  const CloseIcon = () => "x";
  
  const UpdateCustomerDrawer = ({fetchCustomers,initialValues,customerId}) => {
    const { isOpen, onOpen, onClose } = useDisclosure();
    return (
      <>
      <Stack>
        <Button 
        leftIcon={<AddIcon />} 
        rounded={"full"} 
        width={175} mt={3} 
        borderColor="green.500"
        _focus={{
          bg: "green.500",
        }}
        onClick={onOpen} >
          Update Customer
        </Button>
      </Stack>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Update Customer</DrawerHeader>
  
            <DrawerBody>
              <UpdateCustomerForm
                  fetchCustomers = {fetchCustomers}
                  initialValues = {initialValues}
                  customerId={customerId}
              />
            </DrawerBody>
  
            <DrawerFooter>
              <Button 
                  leftIcon={<CloseIcon />} 
                  colorScheme="teal" 
                  onClick={onClose}>
                Close
              </Button>
            </DrawerFooter>
          </DrawerContent>
        </Drawer>
      
      </>

    );
  };
  
  export default UpdateCustomerDrawer;
  