import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
  } from '@chakra-ui/react';
import AlertDialogExample from './DeleteCustomer';
import UpdateCustomerDrawer from './UpdateCustomerDrawer';
  
  export default function CardWithImage({id,name,email,age,gender, imageNumber, fetchCustomers}) {

    const randomUserGender = gender === "MALE" ? "men" : "women"; 

    return (
      <Center py={6}>
        <Box
          maxW={'300px'}
          minW={'300px'}
          w={'full'}
          m={2}
          bg={useColorModeValue('white', 'gray.800')}
          boxShadow={'lg'}
          rounded={'md'}
          overflow={'hidden'}>
          <Image
            h={'120px'}
            w={'full'}
            src={
                'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
            }
            objectFit={'cover'}
          />
          <Flex justify={'center'} mt={-12}>
            <Avatar
              size={'xl'}
              src={
                `https://randomuser.me/api/portraits/med/${randomUserGender}/${imageNumber}.jpg`
              }
              alt={'Author'}
              css={{
                border: '2px solid white',
              }}
            />
          </Flex>
  
          <Box p={6}>
            <Stack spacing={2} align={'center'} mb={5}>
            <Tag borderRadius={"full"}>{id}</Tag>
              <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                {name}
              </Heading>
              <Text color={'gray.500'}>{email}</Text>
              <Text color={'gray.500'}>Age {age} | {gender}</Text>
            </Stack>
            <Stack direction={'row'} justify={'center'}  spacing={2} p={2}>
              
              <UpdateCustomerDrawer 
              fetchCustomers={fetchCustomers}
              initialValues={{name,email, age}} 
              customerId ={id}/>

            <AlertDialogExample customer={{id,name}} fetchCustomers={fetchCustomers}/>  
            </Stack>

            
          </Box>
        </Box>
      </Center>
    );
  }