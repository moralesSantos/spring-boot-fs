import {
  Button,
  useDisclosure,
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
  Stack,
} from "@chakra-ui/react";
import React from "react";
import { deleteCustomer } from "../services/client";
import {
  successNotification,
  errorNotification,
} from "../services/notification";

const AlertDialogExample = ({ customer, fetchCustomers }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const cancelRef = React.useRef();

  const deleteItem = () => {
    deleteCustomer(customer.id)
      .then((res) => {
        console.log(res);
        successNotification(
          "Customer with id:",
          `${customer.id} was succesfully dated`
        );
        fetchCustomers();
      })
      .catch((err) => {
        console.log(err);
        errorNotification(err.code, err.response.message);
      })
      .finally(() => {
        onClose();
      });
  };

  return (
    <>
      <Stack mt={3}>
        <Button
          bg={"red.400"}
          color={"white"}
          rounded={"full"}
          _hover={{
            transform: "translateY(-2px)",
            boxShadow: "lg",
          }}
          _focus={{
            bg: "green.500",
          }}
          colorScheme="red"
          onClick={onOpen}
        >
          Delete
        </Button>
      </Stack>

      <AlertDialog
        isOpen={isOpen}
        leastDestructiveRef={cancelRef}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Customer
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure you want to delete {customer.name}
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button
                colorScheme="red"
                onClick={() => {
                  deleteItem();
                }}
                ml={3}
              >
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </>
  );
};

export default AlertDialogExample;
