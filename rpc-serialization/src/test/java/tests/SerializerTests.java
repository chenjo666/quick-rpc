package tests;

public class SerializerTests {


    // bug
//    @Test
//    public void testHessian() {
//        RpcMessage rpcMessage = RpcMessage.builder()
//                .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
//                .version(RpcConstant.MESSAGE_VERSION)
//                .reserve(RpcConstant.MESSAGE_RESERVE)
//                .messageType(RpcConstant.MESSAGE_TYPE_REQUEST)
//                .serializeTpe(Serializer.SERIALIZER_HESSIAN)
//                .data(new RpcRequest())
//                .build();
//        byte[] bytes = new HessianSerializer().serialize(rpcMessage);
//        RpcMessage deserialize = new HessianSerializer().deserialize(RpcMessage.class, bytes);
//        System.out.println(deserialize);
//    }
}
