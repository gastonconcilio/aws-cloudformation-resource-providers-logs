package com.vg.logs.querydefinition;

import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.cloudwatchlogs.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.*;
import software.amazon.cloudformation.proxy.*;

public class UpdateHandler extends BaseHandlerStd {
    private Logger logger;

    protected ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<CloudWatchLogsClient> proxyClient,
            final Logger logger) {

        this.logger = logger;

        final ResourceModel model = request.getDesiredResourceState();
        final ResourceModel previousModel = request.getPreviousResourceState();

        this.logger.log(String.format("Trying to update model %s", model.getPrimaryIdentifier()));

        return proxy.initiate("AWS-Logs-QueryDefinitions::Update", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToUpdateRequest)
                .makeServiceCall((r, c) -> updateResource(model, r, c))
                .success();
    }

    private PutQueryDefinitionResponse updateResource(
            final ResourceModel model,
            final PutQueryDefinitionRequest awsRequest,
            final ProxyClient<CloudWatchLogsClient> proxyClient) {
        PutMetricFilterResponse awsResponse;
        try {
            boolean exists = exists(proxyClient, model);
            if (!exists) {
                throw new CfnNotFoundException(ResourceModel.TYPE_NAME, model.getPrimaryIdentifier().toString());
            }
            logger.log(String.format("%s has successfully been updated.", ResourceModel.TYPE_NAME));
            return proxyClient.injectCredentialsAndInvokeV2(awsRequest, proxyClient.client()::putQueryDefinition);
        } catch (final ResourceNotFoundException e) {
            logger.log("Resource not found. " + e.getMessage());
            throw new CfnNotFoundException(e);
        } catch (final InvalidParameterException e) {
            throw new CfnInvalidRequestException(ResourceModel.TYPE_NAME, e);
        } catch (final LimitExceededException e) {
            throw new CfnServiceLimitExceededException(e);
        } catch (final ServiceUnavailableException e) {
            throw new CfnServiceInternalErrorException(e);
        } catch (final OperationAbortedException e) {
            throw new CfnResourceConflictException(e);
        }
    }
}
