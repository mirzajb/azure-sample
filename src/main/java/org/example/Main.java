package org.example;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.compute.models.VirtualMachine;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Service");

        readAllVirtualMachines();

    }


    public static void readAllVirtualMachines() {
        String ClientID = "";
        String ClientSecret = "";
        String TenantID = "";
        String SubscriptionId="";
        // Authentication
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(ClientID)
                .clientSecret(ClientSecret)
                .tenantId(TenantID)
                .build();
        AzureResourceManager azureResourceManager = AzureResourceManager
                .configure()
                .withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(clientSecretCredential, profile)
                .withSubscription(SubscriptionId);
        PagedIterable<VirtualMachine> virtualMachines =  azureResourceManager.virtualMachines().list();

        // looping over virtual machines
        virtualMachines.iterableByPage().forEach(resp -> {
            resp.getElements().forEach(vm -> {
                System.out.println("---------------------------");
                System.out.printf("Virtual Machine Name %s \n", vm.name());
                System.out.printf("Virtual computerName %s \n", vm.computerName());
                System.out.printf("Virtual Machine Private IP %s \n", vm.getPrimaryNetworkInterface().primaryIPConfiguration().privateIpAddress());
                System.out.println("---------------------------");
            });
        });


    }
}