package com.vg.logs.querydefinition;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.cloudwatchlogs.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Translator {

    public static BaseHandlerException translateException(final AwsServiceException e) {
        if (e instanceof LimitExceededException) {
            return new CfnServiceLimitExceededException(e);
        }
        if (e instanceof OperationAbortedException) {
            return new CfnResourceConflictException(e);
        }
        if (e instanceof InvalidParameterException) {
            return new CfnInvalidRequestException(e);
        }
        else if (e instanceof ResourceNotFoundException) {
            return new CfnNotFoundException(e);
        }
        else if (e instanceof ServiceUnavailableException) {
            return new CfnServiceInternalErrorException(e);
        }
        return new CfnGeneralServiceException(e);
    }

    static PutQueryDefinitionRequest translateToCreateRequest(final ResourceModel model) {
        return PutQueryDefinitionRequest.builder()
                .logGroupNames(model.getLogGroupNames())
                .name(model.getName())
                .queryDefinitionId(model.getQueryDefinitionId())
                .queryString(model.getQueryString())
                .build();
    }

    static DescribeQueryDefinitionsRequest translateToReadRequest(final ResourceModel model) {
        return DescribeQueryDefinitionsRequest.builder()
                .queryDefinitionNamePrefix(model.getName())
                .maxResults(1)
                .build();
    }

    static DeleteQueryDefinitionRequest translateToDeleteRequest(final ResourceModel model) {
        return DeleteQueryDefinitionRequest.builder()
                .queryDefinitionId(model.getQueryDefinitionId())
                .build();
    }

    static PutQueryDefinitionRequest translateToUpdateRequest(final ResourceModel model) {
        return translateToCreateRequest(model);
    }

    static DescribeQueryDefinitionsRequest translateToListRequest(final String nextToken) {
        return DescribeQueryDefinitionsRequest.builder()
                .nextToken(nextToken)
                .maxResults(50)
                .build();
    }

    static ResourceModel translateFromReadResponse(final DescribeQueryDefinitionsResponse awsResponse) {
        return awsResponse.queryDefinitions()
                .stream()
                .map(Translator::translateQueryDefinition)
                .findFirst()
                .get();
    }

    static ResourceModel translateQueryDefinition (final QueryDefinition queryDefinition) {
        return ResourceModel.builder()
                .name(queryDefinition.name())
                .queryDefinitionId(queryDefinition.queryDefinitionId())
                .queryString(queryDefinition.queryString())
                .logGroupNames(queryDefinition.logGroupNames())
                .build();
    }

    static List<ResourceModel> translateFromListResponse(final DescribeQueryDefinitionsResponse awsResponse) {
        return streamOfOrEmpty(awsResponse.queryDefinitions())
                .map(Translator::translateQueryDefinition)
                .collect(Collectors.toList());
    }

    private static <T> Stream<T> streamOfOrEmpty(final Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream)
                .orElseGet(Stream::empty);
    }
}
