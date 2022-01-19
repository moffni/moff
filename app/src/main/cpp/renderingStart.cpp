#include <jni.h>
#include <string>
#include <dlfcn.h>
#include <los_android_native_app_glue.h>



#define LOAD_HARD(nama) nullptr


#include <vulkan/vulkan.h>
#include <vulkan/vulkan_core.h>
#include <android/log.h>
#include <android/native_window.h>
#include <vector>
#include <string>

#include <thread>
#include <concepts>
#include <experimental/coroutine>
#include <experimental/algorithm>
#include <execution>

#include <experimental/memory_resource>

//#include <module.modulemap>

/*import Foo;
import std.core;
using namespace std;*/
//improt module modulemap
//#include <execution>
// jthread


//#include <source_location>
//template <typename... Ts>
//void log(Ts&&... ts, const std::source_location& loc = std::source_location::current()) {
//    std::cout << loc.function_name() << " line " << loc.line() << ": ";
//    ((std::cout << std::forward<Ts>(ts) << " "), ...);
//    std::cout << '\n';
//}
//
//int main() {
//    log<int, int, std::string>(42, 100, "hello world");
//    log<double, std::string>(10.75, "an important parameter");
//}

#define logRun(...) ((void)__android_log_print(ANDROID_LOG_INFO, "RunGame", __VA_ARGS__))

#if(__ANDROID_API__ >= 30)
#include <android/thermal.h>
#else

#endif

struct myVulkan{
public:

     myVulkan() : mainInstance(VK_NULL_HANDLE) {

     }

    VkInstance  mainInstance;
};


//extern PFN_vkCreateDebugReportCallbackEXT FpvkCreateDebugReportCallbackEXT;



struct CheckVulkan {

    // checking Vulkan
public:

     CheckVulkan() {
          losLibrary = dlopen("libvulkan.so", RTLD_NOW | RTLD_LOCAL);
         if (!losLibrary)
             losLibrary = dlopen("libvulkan.so.1", RTLD_NOW | RTLD_LOCAL);
     }

    PFN_vkEnumerateInstanceVersion vkEnumerateInstanceVersion = LOAD_HARD(vkEnumerateInstanceVersion);
    PFN_vkEnumerateInstanceExtensionProperties vkEnumerateInstanceExtensionProperties =
            LOAD_HARD(vkEnumerateInstanceExtensionProperties);
    PFN_vkEnumerateInstanceLayerProperties vkEnumerateInstanceLayerProperties = LOAD_HARD(vkEnumerateInstanceLayerProperties);
    PFN_vkCreateInstance vkCreateInstance = LOAD_HARD(vkCreateInstance);
    PFN_vkGetInstanceProcAddr vkGetInstanceProcAddr = LOAD_HARD(vkGetInstanceProcAddr);
    PFN_vkDestroyInstance vkDestroyInstance = LOAD_HARD(vkDestroyInstance);
    //PFN_vkCreateDebugReportCallbackEXT vkCreateDebugReportCallbackEXT = LOAD_HARD(vkCreateDebugReportCallbackEXT);
    PFN_vkDebugReportCallbackEXT vkDebugReportCallbackEXT = LOAD_HARD(vkDebugReportCallbackEXT);

    void initializedAll(){


          Load(vkEnumerateInstanceVersion, "vkEnumerateInstanceVersion");
          Load(vkEnumerateInstanceExtensionProperties, "vkEnumerateInstanceExtensionProperties");
          Load(vkEnumerateInstanceLayerProperties, "vkEnumerateInstanceLayerProperties");
          Load(vkCreateInstance, "vkCreateInstance");
          Load(vkDestroyInstance, "vkDestroyInstance");

          // vkGetInstanceProcAddr
          Load(vkGetInstanceProcAddr, "vkGetInstanceProcAddr");

          // extension not loading ??
         // Load(vkDebugReportCallbackEXT, "vkDebugReportCallbackEXT");
         // Load(vkCreateDebugReportCallbackEXT, "vkCreateDebugReportCallbackEXT");





      }

    template <typename T>
    void Load(T& func_dest, const char* func_name) {
        func_dest = reinterpret_cast<T>(dlsym(losLibrary, func_name));

    }


private:
    void *losLibrary;
};




//extern "C" JNIEXPORT jstring JNICALL
//Java_com_sergey_los_freelanceideas_renderings_MainActivity_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {


inline void cb(VkResult myResult ) noexcept {

     if(myResult == VK_SUCCESS){
         logRun(" vk_success is very all good ");
     }else{
         switch (myResult) {
             case VK_NOT_READY:
                 logRun (  " vk not ready ! \n");
                 break;

             case VK_INCOMPLETE:
                 logRun (  " vk is not Incompete \n");
                 break;

             case  VK_EVENT_SET:
                 logRun (  " vk event set \n");
                 break;

             case VK_EVENT_RESET:
                 logRun (  " vk event reset \n");
                 break;

                 /*     case VK_INCOMPLETE:
                      logRun (  " vk incomplete \n";
                        break;*/

             case VK_ERROR_OUT_OF_HOST_MEMORY:
                 logRun (  " VK_ERROR_OUT_OF_HOST_MEMORY  this error \n");
                 break;


             case VK_ERROR_OUT_OF_DEVICE_MEMORY:
                 logRun (  " VK_ERROR_OUT_OF_DEVICE_MEMORY error \n");
                 break;


             case  VK_ERROR_INITIALIZATION_FAILED:
                 logRun (  " VK_ERROR_INITIALIZATION_FAILED error \n");
                 break;

             case VK_ERROR_DEVICE_LOST:
                 logRun (  "VK_ERROR_DEVICE_LOST \n");
                 break;

             case VK_ERROR_MEMORY_MAP_FAILED:
                 logRun (  "VK_ERROR_MEMORY_MAP_FAILED \n");
                 break;

             case VK_ERROR_LAYER_NOT_PRESENT:
                 logRun (  " VK_ERROR_LAYER_NOT_PRESENT \n");
                 break;

             case VK_ERROR_EXTENSION_NOT_PRESENT:
                 logRun (  " VK_ERROR_EXTENSION_NOT_PRESENT \n");
                 break;


             case VK_ERROR_FEATURE_NOT_PRESENT:
                 logRun (  "VK_ERROR_FEATURE_NOT_PRESENT \n");
                 break;


             case VK_ERROR_INCOMPATIBLE_DRIVER:
                 logRun ( "VK_ERROR_INCOMPATIBLE_DRIVER \n");
                 break;

             case VK_ERROR_TOO_MANY_OBJECTS:
                 logRun (  "VK_ERROR_TOO_MANY_OBJECTS  \n");
                 break;


             case VK_ERROR_FORMAT_NOT_SUPPORTED:
                 logRun (  " VK_ERROR_FORMAT_NOT_SUPPORTED error \n");
                 break;

             case VK_ERROR_FRAGMENTED_POOL:
                 logRun (  "  VK_ERROR_FRAGMENTED_POOL \n");
                 break;

//             case VK_ERROR_UNKNOWN:
//                 logRun (  "VK_ERROR_UNKNOWN \n");
//                 break;



             case VK_ERROR_OUT_OF_POOL_MEMORY:
                 logRun (  "VK_ERROR_OUT_OF_POOL_MEMORY \n");
                 break;


             case VK_ERROR_INVALID_EXTERNAL_HANDLE:
                 logRun (  "VK_ERROR_INVALID_EXTERNAL_HANDLE \n");
                 break;

//             case VK_ERROR_FRAGMENTATION:
//                 logRun (  " eror m y VK_ERROR_FRAGMENTATION \n");
//                 break;


//             case VK_ERROR_INVALID_OPAQUE_CAPTURE_ADDRESS:
//                 logRun (  "VK_ERROR_INVALID_OPAQUE_CAPTURE_ADDRESS \n");
//                 break;


             case VK_ERROR_SURFACE_LOST_KHR:
                 logRun (  "VK_ERROR_SURFACE_LOST_KHR \n");
                 break;



             case VK_ERROR_NATIVE_WINDOW_IN_USE_KHR:
                 logRun (  "VK_ERROR_NATIVE_WINDOW_IN_USE_KHR \n");
                 break;


             case VK_SUBOPTIMAL_KHR:
                 logRun (  "VK_SUBOPTIMAL_KHR \n");
                 break;



             case VK_ERROR_OUT_OF_DATE_KHR:
                 logRun (  "VK_ERROR_OUT_OF_DATE_KHR \n");
                 break;


             case VK_ERROR_INCOMPATIBLE_DISPLAY_KHR:
                 logRun (  "VK_ERROR_INCOMPATIBLE_DISPLAY_KHR \n");
                 break;
                 // VK_ERROR_OUT_OF_POOL_MEMORY = -1000069000,
                 // VK_ERROR_INVALID_EXTERNAL_HANDLE = -1000072003,
                 // VK_ERROR_FRAGMENTATION = -1000161000,

             case VK_ERROR_VALIDATION_FAILED_EXT:
                 logRun (  "VK_ERROR_VALIDATION_FAILED_EXT \n");
                 break;


             case VK_ERROR_INVALID_SHADER_NV:
                 logRun (  "VK_ERROR_INVALID_SHADER_NV \n");
                 break;


             case VK_ERROR_INVALID_DRM_FORMAT_MODIFIER_PLANE_LAYOUT_EXT:
                 logRun (  "VK_ERROR_INVALID_DRM_FORMAT_MODIFIER_PLANE_LAYOUT_EXT \n");
                 break;


             case VK_ERROR_NOT_PERMITTED_EXT:
                 logRun (  "VK_ERROR_NOT_PERMITTED_EXT \n");
                 break;



             case VK_ERROR_FULL_SCREEN_EXCLUSIVE_MODE_LOST_EXT:
                 logRun (  "VK_ERROR_FULL_SCREEN_EXCLUSIVE_MODE_LOST_EXT \n");
                 break;



//             case VK_THREAD_IDLE_KHR:
//                 logRun (  "VK_THREAD_IDLE_KHR \n");
//                 break;


//             case VK_THREAD_DONE_KHR:
//                 logRun (  "VK_THREAD_DONE_KHR \n");
//                 break;
//
//
//
//             case VK_OPERATION_DEFERRED_KHR:
//                 logRun (  "VK_OPERATION_DEFERRED_KHR \n");
//                 break;
//
//
//
//             case VK_OPERATION_NOT_DEFERRED_KHR:
//                 logRun (  "VK_OPERATION_NOT_DEFERRED_KHR \n");
//                 break;
//
//
//             case VK_PIPELINE_COMPILE_REQUIRED_EXT:
//                 logRun (  "VK_PIPELINE_COMPILE_REQUIRED_EXT");
//                 break;

             default:
                 logRun (  " not found error's \n");
         }
     }


}



std::string_view to_string( VkResult r ){
    switch( r ){
        case VK_SUCCESS: return "VK_SUCCESS";
        case VK_NOT_READY: return "VK_NOT_READY";
        case VK_TIMEOUT: return "VK_TIMEOUT";
        case VK_EVENT_SET: return "VK_EVENT_SET";
        case VK_EVENT_RESET: return "VK_EVENT_RESET";
        case VK_INCOMPLETE: return "VK_INCOMPLETE";
        case VK_ERROR_OUT_OF_HOST_MEMORY: return "VK_ERROR_OUT_OF_HOST_MEMORY";
        case VK_ERROR_OUT_OF_DEVICE_MEMORY: return "VK_ERROR_OUT_OF_DEVICE_MEMORY";
        case VK_ERROR_INITIALIZATION_FAILED: return "VK_ERROR_INITIALIZATION_FAILED";
        case VK_ERROR_DEVICE_LOST: return "VK_ERROR_DEVICE_LOST";
        case VK_ERROR_MEMORY_MAP_FAILED: return "VK_ERROR_MEMORY_MAP_FAILED";
        case VK_ERROR_LAYER_NOT_PRESENT: return "VK_ERROR_LAYER_NOT_PRESENT";
        case VK_ERROR_EXTENSION_NOT_PRESENT: return "VK_ERROR_EXTENSION_NOT_PRESENT";
        case VK_ERROR_FEATURE_NOT_PRESENT: return "VK_ERROR_FEATURE_NOT_PRESENT";
        case VK_ERROR_INCOMPATIBLE_DRIVER: return "VK_ERROR_INCOMPATIBLE_DRIVER";
        case VK_ERROR_TOO_MANY_OBJECTS: return "VK_ERROR_TOO_MANY_OBJECTS";
        case VK_ERROR_FORMAT_NOT_SUPPORTED: return "VK_ERROR_FORMAT_NOT_SUPPORTED";
        case VK_ERROR_SURFACE_LOST_KHR: return "VK_ERROR_SURFACE_LOST_KHR";
        case VK_ERROR_NATIVE_WINDOW_IN_USE_KHR: return "VK_ERROR_NATIVE_WINDOW_IN_USE_KHR";
        case VK_SUBOPTIMAL_KHR: return "VK_SUBOPTIMAL_KHR";
        case VK_ERROR_OUT_OF_DATE_KHR: return "VK_ERROR_OUT_OF_DATE_KHR";
        case VK_ERROR_INCOMPATIBLE_DISPLAY_KHR: return "VK_ERROR_INCOMPATIBLE_DISPLAY_KHR";
        case VK_ERROR_VALIDATION_FAILED_EXT: return "VK_ERROR_VALIDATION_FAILED_EXT";
        case VK_ERROR_INVALID_SHADER_NV: return "VK_ERROR_INVALID_SHADER_NV";
        default: return "unrecognized VkResult code";
    }
}


std::string_view to_string( VkDebugReportObjectTypeEXT o ){
    switch( o ){
        case VK_DEBUG_REPORT_OBJECT_TYPE_UNKNOWN_EXT: return "unknown";
        case VK_DEBUG_REPORT_OBJECT_TYPE_INSTANCE_EXT: return "Instance";
        case VK_DEBUG_REPORT_OBJECT_TYPE_PHYSICAL_DEVICE_EXT: return "PhysicalDevice";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DEVICE_EXT: return "Device";
        case VK_DEBUG_REPORT_OBJECT_TYPE_QUEUE_EXT: return "Queue";
        case VK_DEBUG_REPORT_OBJECT_TYPE_SEMAPHORE_EXT: return "Semaphore";
        case VK_DEBUG_REPORT_OBJECT_TYPE_COMMAND_BUFFER_EXT: return "CommandBuffer";
        case VK_DEBUG_REPORT_OBJECT_TYPE_FENCE_EXT: return "Fence";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DEVICE_MEMORY_EXT: return "DeviceMemory";
        case VK_DEBUG_REPORT_OBJECT_TYPE_BUFFER_EXT: return "Buffer";
        case VK_DEBUG_REPORT_OBJECT_TYPE_IMAGE_EXT: return "Image";
        case VK_DEBUG_REPORT_OBJECT_TYPE_EVENT_EXT: return "Event";
        case VK_DEBUG_REPORT_OBJECT_TYPE_QUERY_POOL_EXT: return "QueryPool";
        case VK_DEBUG_REPORT_OBJECT_TYPE_BUFFER_VIEW_EXT: return "BufferView";
        case VK_DEBUG_REPORT_OBJECT_TYPE_IMAGE_VIEW_EXT: return "ImageView";
        case VK_DEBUG_REPORT_OBJECT_TYPE_SHADER_MODULE_EXT: return "ShaderModule";
        case VK_DEBUG_REPORT_OBJECT_TYPE_PIPELINE_CACHE_EXT: return "PipelineCache";
        case VK_DEBUG_REPORT_OBJECT_TYPE_PIPELINE_LAYOUT_EXT: return "PipelineLayout";
        case VK_DEBUG_REPORT_OBJECT_TYPE_RENDER_PASS_EXT: return "RenderPass";
        case VK_DEBUG_REPORT_OBJECT_TYPE_PIPELINE_EXT: return "Pipeline";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DESCRIPTOR_SET_LAYOUT_EXT: return "DescriptorSetLayout";
        case VK_DEBUG_REPORT_OBJECT_TYPE_SAMPLER_EXT: return "Sampler";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DESCRIPTOR_POOL_EXT: return "DescriptorPool";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DESCRIPTOR_SET_EXT: return "DescriptorSet";
        case VK_DEBUG_REPORT_OBJECT_TYPE_FRAMEBUFFER_EXT: return "Framebuffer";
        case VK_DEBUG_REPORT_OBJECT_TYPE_COMMAND_POOL_EXT: return "Command pool";
        case VK_DEBUG_REPORT_OBJECT_TYPE_SURFACE_KHR_EXT: return "SurfaceKHR";
        case VK_DEBUG_REPORT_OBJECT_TYPE_SWAPCHAIN_KHR_EXT: return "SwapchainKHR";
        case VK_DEBUG_REPORT_OBJECT_TYPE_DEBUG_REPORT_EXT: return "DebugReport";
        default: return "unrecognized";
    }
}


VKAPI_ATTR VkBool32 VKAPI_CALL genericDebugCallback(
        VkDebugReportFlagsEXT msgFlags,
        VkDebugReportObjectTypeEXT objType,
        uint64_t srcObject,
        size_t /*location*/,
        int32_t msgCode,
        const char* pLayerPrefix,
        const char* pMsg,
        void* /*pUserData*/
);


VKAPI_ATTR VkBool32 VKAPI_CALL genericDebugCallback(
        VkDebugReportFlagsEXT msgFlags,    // was VkDebugReportFlagsEXT
        VkDebugReportObjectTypeEXT objType,
        uint64_t srcObject,
        size_t /*location*/,
        int32_t msgCode,
        const char* pLayerPrefix,
        const char* pMsg,
        void* /*pUserData*/
){
    // just print everything
     logRun(" ours Debugging messages android  %s \n", pMsg);


    std::string report = std::string(to_string( objType )) + ", " + pLayerPrefix + ", " + pMsg;

    switch( msgFlags ){
        case VK_DEBUG_REPORT_INFORMATION_BIT_EXT:
              logRun("Info: %s \n",  report.c_str() );
            break;

        case VK_DEBUG_REPORT_WARNING_BIT_EXT:
            logRun("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            logRun("WARNING: %s \n", report.c_str());
            logRun ( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            break;

        case VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT:
             logRun( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
             logRun("PERFORMANCE: %s \n", report.c_str() );
             logRun("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            break;

        case VK_DEBUG_REPORT_ERROR_BIT_EXT:
            logRun( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            logRun("ERROR: %s \n ", report.c_str());
            logRun("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            break;

        case VK_DEBUG_REPORT_DEBUG_BIT_EXT:
            report += "Debug: ";
            break;
    }





    return VK_FALSE;
}


/*VKAPI_ATTR VkResult VKAPI_CALL vkCreateDebugReportCallbackEXT_loader(
        VkInstance instance,
        const VkDebugReportCallbackCreateInfoEXT* pCreateInfo,
        const VkAllocationCallbacks* pAllocator,
        VkDebugReportCallbackEXT* pCallback
){
    PFN_vkVoidFunction temp_fp = vkGetInstanceProcAddr( instance, "vkCreateDebugReportCallbackEXT" );
    if( !temp_fp ) throw  "Failed to load vkCreateDebugReportCallbackEXT";

    FpvkCreateDebugReportCallbackEXT = reinterpret_cast<PFN_vkCreateDebugReportCallbackEXT>( temp_fp );

    return vkCreateDebugReportCallbackEXT( instance, pCreateInfo, pAllocator, pCallback );
}*/

//PFN_vkCreateDebugReportCallbackEXT FpvkCreateDebugReportCallbackEXT = &vkCreateDebugReportCallbackEXT_loader;


/*VKAPI_ATTR VkResult VKAPI_CALL vkCreateDebugReportCallbackEXT(
        VkInstance instance,
        const VkDebugReportCallbackCreateInfoEXT* pCreateInfo,
        const VkAllocationCallbacks* pAllocator,
        VkDebugReportCallbackEXT* pCallback
){
    return FpvkCreateDebugReportCallbackEXT( instance, pCreateInfo, pAllocator, pCallback );
}*/






void android_main(struct android_app* state){

#if(__ANDROID_API__ >= 30)
    logRun(" bigger 30 ! \n");
    AThermalManager* losThermal = AThermal_acquireManager();

    AThermal_getCurrentThermalStatus(losThermal);

#else
    logRun(" smaller 30 ! \n");
#endif





    //

    myVulkan vulkanA = myVulkan();
     bool debugUtilsExists =false;
     bool debugReport = false;
     bool surface_capa2 = false;
     bool phDeviceProperty2 = false;


     // ANativeWindow* lef =  state->window;

     //   lef->setFra
     //   lef->setFra

//       if(state->pendingWindow == nullptr){
//           logRun(" error s \n");
//       }
//
//     logRun(" my width screen == %d \n ", ANativeWindow_getWidth(state->pendingWindow));


    CheckVulkan* check = new CheckVulkan();
        check->initializedAll();

        VkResult res;
        uint32_t versionVulkan = 0;
        uint32_t currentVersionVulkan;
       res = check->vkEnumerateInstanceVersion(&versionVulkan);

        if(res != VK_SUCCESS){
           logRun(" not loaded Vulkan is ");
        }else{
             logRun(" loadede Vulkan's !! " );
              uint16_t lMajor = VK_VERSION_MAJOR(versionVulkan);
              uint16_t lMijor = VK_VERSION_MINOR(versionVulkan);
              logRun(" My vulkan version's == %d.%d", lMajor, lMijor);
               if(lMijor == 2 ){
                   currentVersionVulkan = VK_VERSION_1_1;
               }else if (lMijor == 1){
                   currentVersionVulkan = VK_VERSION_1_1;
               }else{
                   currentVersionVulkan = VK_VERSION_1_0;
               }
        }


//          auto opengl_info = {GL_VENDOR, GL_RENDERER, GL_VERSION, GL_EXTENSIONS};
//    for (auto name : opengl_info) {
//        auto info = glGetString(name);
//        logRun("OpenGL Info: %s", info);
//    }
         //

        // vkEnumerateInstanceExtensionProperties

         uint32_t instancExtensCount = 0;
         res = check->vkEnumerateInstanceExtensionProperties(nullptr, &instancExtensCount, nullptr);
         VkExtensionProperties* instaProperty = new VkExtensionProperties [instancExtensCount];
         res = check->vkEnumerateInstanceExtensionProperties(nullptr, &instancExtensCount, instaProperty);

           if(res != VK_SUCCESS){

                logRun(" error this 01 ");
           }

           std::vector<const char*> extensionForAdd;
           for(auto i = 0; i < instancExtensCount; i++){
                logRun(" my extension names == %s \n", instaProperty[i].extensionName);
                 extensionForAdd.push_back(instaProperty[i].extensionName);

                    if( strcmp(instaProperty[i].extensionName, "VK_EXT_debug_utils") == 0){
                        logRun(" Exists DEBUG_utils  \n");
                        debugUtilsExists = true;
                    }

                    if( strcmp(instaProperty[i].extensionName, "VK_EXT_debug_report") == 0){
                         logRun(" Exists debug_report \n");
                        debugReport = true;
                    }

                    // VK_KHR_get_surface_capabilities2
                    if( strcmp(instaProperty[i].extensionName, "VK_KHR_get_surface_capabilities2") == 0){
                        logRun(" Exists Surface_capability_2 ! \n");
                        surface_capa2 = true;
                    }

                    if( strcmp(instaProperty[i].extensionName, "VK_KHR_get_physical_device_properties2") == 0){
                         logRun(" Exists Physical Device Property2 \n");
                        phDeviceProperty2 = true;
                    }
           }


    VkApplicationInfo applicationInfo = {};
    applicationInfo.sType = VK_STRUCTURE_TYPE_APPLICATION_INFO;
    applicationInfo.pNext = nullptr;
    applicationInfo.pApplicationName = "LosRenderings";
    applicationInfo.applicationVersion = 0;
    applicationInfo.pEngineName = "EngineRender";
    applicationInfo.engineVersion = 1;
    applicationInfo.apiVersion = currentVersionVulkan;


     // layers Vulkan a


     uint32_t numInstanceLayers = 0;
     check->vkEnumerateInstanceLayerProperties(&numInstanceLayers, nullptr);
     logRun(" number layers count's  == %d \n", numInstanceLayers);
     VkLayerProperties* layerProperties = (VkLayerProperties*)malloc(numInstanceLayers * sizeof(VkLayerProperties));
     check->vkEnumerateInstanceLayerProperties(&numInstanceLayers, layerProperties);

      std::vector<const char*> layName;
      layName.push_back("VK_LAYER_KHRONOS_validation");



         if(!debugUtilsExists){
              logRun(" Debug is ok ! ");
                    if(debugReport){

                    }else{
                   logRun(" no debug ! \n");
                    }
         }
     


           //tension names == VK_KHR_surfac

           // TODO: samsung s21+
    // VK_KHR_android_surface
    // VK_EXT_swapchain_colorspace
    // VK_KHR_get_surface_capabilities2
    // VK_EXT_debug_report
    // VK_KHR_device_group_creation
    // VK_KHR_external_fence_capabilities
    // VK_KHR_external_memory_capabilities
    // VK_KHR_external_semaphore_capabilities
    // VK_KHR_get_physical_device_properties2

     // TODO: Realme 6
// VK_KHR_surface
// VK_KHR_android_surface
// VK_EXT_swapchain_colorspace
// VK_KHR_get_surface_capabilities2
// VK_EXT_debug_report
// VK_KHR_external_fence_capabilities
// VK_KHR_external_memory_capabilities
// VK_KHR_external_semaphore_capabilities
// VK_KHR_get_physical_device_properties2
// VK_KHR_device_group_creation

      // TODO: Xiaomi 8 Lite
    // VK_KHR_surface
    // VK_KHR_android_surface
    // VK_EXT_swapchain_colorspace
    // VK_KHR_get_surface_capabilities2
    // VK_EXT_debug_report
    // VK_KHR_get_physical_device_properties2
    // VK_KHR_external_semaphore_capabilities
    // VK_KHR_external_memory_capabilities
    // VK_KHR_device_group_creation
    // VK_KHR_external_fence_capabilities

    VkInstanceCreateInfo instanceInfo = {};
         instanceInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
         instanceInfo.pNext = nullptr;
         instanceInfo.pApplicationInfo = &applicationInfo;
         instanceInfo.enabledLayerCount = 1;
         instanceInfo.ppEnabledLayerNames = layName.data();
         instanceInfo.enabledExtensionCount = instancExtensCount;
         instanceInfo.ppEnabledExtensionNames = extensionForAdd.data();

         res = check->vkCreateInstance(&instanceInfo, nullptr, &vulkanA.mainInstance);

         // if debug exists
        cb(res);




           VkDebugReportCallbackEXT  cb1, cb2, cb3;
           // VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT
           VkDebugReportCallbackCreateInfoEXT callback1 = {
                   VK_STRUCTURE_TYPE_DEBUG_REPORT_CREATE_INFO_EXT,
                   nullptr,
                   VK_DEBUG_REPORT_ERROR_BIT_EXT | VK_DEBUG_REPORT_WARNING_BIT_EXT,
                   genericDebugCallback,
                   nullptr
           };


           // std::experimental::pmr::polymorphic_allocator sdf(); // ok this



   // PFN_vkCreateDebugReportCallbackEXT FpvkCreateDebugReportCallbackEXT = check->vkCreateDebugReportCallbackEXT_loader;



   // PFN_vkCreateDebugReportCallbackEXT FpvkCreateDebugReportCallbackEXT = &vkCreateDebugReportCallbackEXT_loader;

    logRun("pre loading vkCreateDebugReportCallbackEXT \n");
     //    res =check->vkCreateDebugReportCallbackEXT(vulkanA.mainInstance,  &callback1, nullptr, &cb1);


    PFN_vkVoidFunction temp_fp = check->vkGetInstanceProcAddr( vulkanA.mainInstance, "vkCreateDebugReportCallbackEXT" );
    if( !temp_fp ) throw  "Failed to load vkCreateDebugReportCallbackEXT";

    PFN_vkCreateDebugReportCallbackEXT FpvkCreateDebugReportCallbackEXT = reinterpret_cast<PFN_vkCreateDebugReportCallbackEXT>( temp_fp );


    PFN_vkVoidFunction temp_fp2 = check->vkGetInstanceProcAddr( vulkanA.mainInstance, "vkDestroyDebugReportCallbackEXT" );
    if( !temp_fp2 ) throw  "Failed to load vkCreateDebugReportCallbackEXT";

    PFN_vkDestroyDebugReportCallbackEXT FpvkDestroyDebugReportCallbackEXT = reinterpret_cast<PFN_vkDestroyDebugReportCallbackEXT>( temp_fp2 );
    FpvkCreateDebugReportCallbackEXT(vulkanA.mainInstance,  &callback1, nullptr, &cb1);




    PFN_vkVoidFunction temp_fp2Enum = check->vkGetInstanceProcAddr( vulkanA.mainInstance, "vkEnumeratePhysicalDevices" );
    if( !temp_fp2Enum ) throw  "Failed to load vkCreateDebugReportCallbackEXT";

     uint32_t  ip; // = memAllocInt(1);
    PFN_vkEnumeratePhysicalDevices vkEnumeratePhysicalDevices1 = reinterpret_cast<PFN_vkEnumeratePhysicalDevices>( temp_fp2Enum );

     res = vkEnumeratePhysicalDevices1(vulkanA.mainInstance, &ip, nullptr);
       logRun(" my pysical devices numbres == %d \n", ip);




      // surface created


    FpvkDestroyDebugReportCallbackEXT(vulkanA.mainInstance, cb1, nullptr);
    logRun("post loading vkCreateDebugReportCallbackEXT \n");



         cb(res);


#if(__ANDROID_API__ >= 30)

#else

#endif



          logRun(" pre Destroy instance \n");


       check->vkDestroyInstance(vulkanA.mainInstance, nullptr);
       vulkanA.mainInstance = VK_NULL_HANDLE;

  //  std::string hello = "Hello from C++";
  //  return env->NewStringUTF(hello.c_str());
}