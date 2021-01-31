package com.vg.logs.querydefinition;

import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.cloudformation.exceptions.*;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<CloudWatchLogsClient> proxyClient,
        final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        return proxy.initiate("AWS-Logs-QueryDefinition", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToCreateRequest)
                .makeServiceCall((r, c) -> createResource(model, r, c))
                .success();
    }

    private PutQueryDefinitionResponse createResource(
            final ResourceModel model,
            final PutQueryDefinitionRequest awsRequest,
            final ProxyClient<CloudWatchLogsClient> proxyClient) {

            try {
                return proxyClient.injectCredentialsAndInvokeV2(awsRequest, proxyClient.client()::putQueryDefinition);
            } catch (final InvalidParameterException e) {
                throw new CfnInvalidRequestException(ResourceModel.TYPE_NAME, e);
            } catch (final LimitExceededException e) {
                throw new CfnServiceLimitExceededException(e);
            } catch (final OperationAbortedException e) {
                throw new CfnResourceConflictException(e);
            } catch (final ServiceUnavailableException e) {
                throw new CfnServiceInternalErrorException(e);
            }
        }
}