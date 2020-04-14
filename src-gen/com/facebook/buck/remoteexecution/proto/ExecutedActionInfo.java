// @generated
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/com/facebook/buck/remoteexecution/proto/metadata.proto

package com.facebook.buck.remoteexecution.proto;

/**
 * <pre>
 * Executed action information
 * </pre>
 *
 * Protobuf type {@code facebook.remote_execution.ExecutedActionInfo}
 */
@javax.annotation.Generated(value="protoc", comments="annotations:ExecutedActionInfo.java.pb.meta")
public  final class ExecutedActionInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:facebook.remote_execution.ExecutedActionInfo)
    ExecutedActionInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ExecutedActionInfo.newBuilder() to construct.
  private ExecutedActionInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ExecutedActionInfo() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ExecutedActionInfo(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            cpuStatUsageUsec_ = input.readInt64();
            break;
          }
          case 16: {

            cpuStatUserUsec_ = input.readInt64();
            break;
          }
          case 24: {

            cpuStatSystemUsec_ = input.readInt64();
            break;
          }
          case 34: {
            com.google.protobuf.BoolValue.Builder subBuilder = null;
            if (isFallbackEnabledForCompletedAction_ != null) {
              subBuilder = isFallbackEnabledForCompletedAction_.toBuilder();
            }
            isFallbackEnabledForCompletedAction_ = input.readMessage(com.google.protobuf.BoolValue.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(isFallbackEnabledForCompletedAction_);
              isFallbackEnabledForCompletedAction_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.facebook.buck.remoteexecution.proto.RemoteExecutionMetadataProto.internal_static_facebook_remote_execution_ExecutedActionInfo_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.facebook.buck.remoteexecution.proto.RemoteExecutionMetadataProto.internal_static_facebook_remote_execution_ExecutedActionInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.class, com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.Builder.class);
  }

  public static final int CPU_STAT_USAGE_USEC_FIELD_NUMBER = 1;
  private long cpuStatUsageUsec_;
  /**
   * <code>int64 cpu_stat_usage_usec = 1;</code>
   */
  public long getCpuStatUsageUsec() {
    return cpuStatUsageUsec_;
  }

  public static final int CPU_STAT_USER_USEC_FIELD_NUMBER = 2;
  private long cpuStatUserUsec_;
  /**
   * <code>int64 cpu_stat_user_usec = 2;</code>
   */
  public long getCpuStatUserUsec() {
    return cpuStatUserUsec_;
  }

  public static final int CPU_STAT_SYSTEM_USEC_FIELD_NUMBER = 3;
  private long cpuStatSystemUsec_;
  /**
   * <code>int64 cpu_stat_system_usec = 3;</code>
   */
  public long getCpuStatSystemUsec() {
    return cpuStatSystemUsec_;
  }

  public static final int IS_FALLBACK_ENABLED_FOR_COMPLETED_ACTION_FIELD_NUMBER = 4;
  private com.google.protobuf.BoolValue isFallbackEnabledForCompletedAction_;
  /**
   * <pre>
   * Whether we should fallback to local retry if this action fails with exit code 1.
   * Fallback means we don't trust if this action failed and it may be flaky.
   * </pre>
   *
   * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
   */
  public boolean hasIsFallbackEnabledForCompletedAction() {
    return isFallbackEnabledForCompletedAction_ != null;
  }
  /**
   * <pre>
   * Whether we should fallback to local retry if this action fails with exit code 1.
   * Fallback means we don't trust if this action failed and it may be flaky.
   * </pre>
   *
   * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
   */
  public com.google.protobuf.BoolValue getIsFallbackEnabledForCompletedAction() {
    return isFallbackEnabledForCompletedAction_ == null ? com.google.protobuf.BoolValue.getDefaultInstance() : isFallbackEnabledForCompletedAction_;
  }
  /**
   * <pre>
   * Whether we should fallback to local retry if this action fails with exit code 1.
   * Fallback means we don't trust if this action failed and it may be flaky.
   * </pre>
   *
   * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
   */
  public com.google.protobuf.BoolValueOrBuilder getIsFallbackEnabledForCompletedActionOrBuilder() {
    return getIsFallbackEnabledForCompletedAction();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (cpuStatUsageUsec_ != 0L) {
      output.writeInt64(1, cpuStatUsageUsec_);
    }
    if (cpuStatUserUsec_ != 0L) {
      output.writeInt64(2, cpuStatUserUsec_);
    }
    if (cpuStatSystemUsec_ != 0L) {
      output.writeInt64(3, cpuStatSystemUsec_);
    }
    if (isFallbackEnabledForCompletedAction_ != null) {
      output.writeMessage(4, getIsFallbackEnabledForCompletedAction());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (cpuStatUsageUsec_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, cpuStatUsageUsec_);
    }
    if (cpuStatUserUsec_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, cpuStatUserUsec_);
    }
    if (cpuStatSystemUsec_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, cpuStatSystemUsec_);
    }
    if (isFallbackEnabledForCompletedAction_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getIsFallbackEnabledForCompletedAction());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.facebook.buck.remoteexecution.proto.ExecutedActionInfo)) {
      return super.equals(obj);
    }
    com.facebook.buck.remoteexecution.proto.ExecutedActionInfo other = (com.facebook.buck.remoteexecution.proto.ExecutedActionInfo) obj;

    if (getCpuStatUsageUsec()
        != other.getCpuStatUsageUsec()) return false;
    if (getCpuStatUserUsec()
        != other.getCpuStatUserUsec()) return false;
    if (getCpuStatSystemUsec()
        != other.getCpuStatSystemUsec()) return false;
    if (hasIsFallbackEnabledForCompletedAction() != other.hasIsFallbackEnabledForCompletedAction()) return false;
    if (hasIsFallbackEnabledForCompletedAction()) {
      if (!getIsFallbackEnabledForCompletedAction()
          .equals(other.getIsFallbackEnabledForCompletedAction())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + CPU_STAT_USAGE_USEC_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCpuStatUsageUsec());
    hash = (37 * hash) + CPU_STAT_USER_USEC_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCpuStatUserUsec());
    hash = (37 * hash) + CPU_STAT_SYSTEM_USEC_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCpuStatSystemUsec());
    if (hasIsFallbackEnabledForCompletedAction()) {
      hash = (37 * hash) + IS_FALLBACK_ENABLED_FOR_COMPLETED_ACTION_FIELD_NUMBER;
      hash = (53 * hash) + getIsFallbackEnabledForCompletedAction().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.facebook.buck.remoteexecution.proto.ExecutedActionInfo prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Executed action information
   * </pre>
   *
   * Protobuf type {@code facebook.remote_execution.ExecutedActionInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:facebook.remote_execution.ExecutedActionInfo)
      com.facebook.buck.remoteexecution.proto.ExecutedActionInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.facebook.buck.remoteexecution.proto.RemoteExecutionMetadataProto.internal_static_facebook_remote_execution_ExecutedActionInfo_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.facebook.buck.remoteexecution.proto.RemoteExecutionMetadataProto.internal_static_facebook_remote_execution_ExecutedActionInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.class, com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.Builder.class);
    }

    // Construct using com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      cpuStatUsageUsec_ = 0L;

      cpuStatUserUsec_ = 0L;

      cpuStatSystemUsec_ = 0L;

      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        isFallbackEnabledForCompletedAction_ = null;
      } else {
        isFallbackEnabledForCompletedAction_ = null;
        isFallbackEnabledForCompletedActionBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.facebook.buck.remoteexecution.proto.RemoteExecutionMetadataProto.internal_static_facebook_remote_execution_ExecutedActionInfo_descriptor;
    }

    @java.lang.Override
    public com.facebook.buck.remoteexecution.proto.ExecutedActionInfo getDefaultInstanceForType() {
      return com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.getDefaultInstance();
    }

    @java.lang.Override
    public com.facebook.buck.remoteexecution.proto.ExecutedActionInfo build() {
      com.facebook.buck.remoteexecution.proto.ExecutedActionInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.facebook.buck.remoteexecution.proto.ExecutedActionInfo buildPartial() {
      com.facebook.buck.remoteexecution.proto.ExecutedActionInfo result = new com.facebook.buck.remoteexecution.proto.ExecutedActionInfo(this);
      result.cpuStatUsageUsec_ = cpuStatUsageUsec_;
      result.cpuStatUserUsec_ = cpuStatUserUsec_;
      result.cpuStatSystemUsec_ = cpuStatSystemUsec_;
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        result.isFallbackEnabledForCompletedAction_ = isFallbackEnabledForCompletedAction_;
      } else {
        result.isFallbackEnabledForCompletedAction_ = isFallbackEnabledForCompletedActionBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.facebook.buck.remoteexecution.proto.ExecutedActionInfo) {
        return mergeFrom((com.facebook.buck.remoteexecution.proto.ExecutedActionInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.facebook.buck.remoteexecution.proto.ExecutedActionInfo other) {
      if (other == com.facebook.buck.remoteexecution.proto.ExecutedActionInfo.getDefaultInstance()) return this;
      if (other.getCpuStatUsageUsec() != 0L) {
        setCpuStatUsageUsec(other.getCpuStatUsageUsec());
      }
      if (other.getCpuStatUserUsec() != 0L) {
        setCpuStatUserUsec(other.getCpuStatUserUsec());
      }
      if (other.getCpuStatSystemUsec() != 0L) {
        setCpuStatSystemUsec(other.getCpuStatSystemUsec());
      }
      if (other.hasIsFallbackEnabledForCompletedAction()) {
        mergeIsFallbackEnabledForCompletedAction(other.getIsFallbackEnabledForCompletedAction());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.facebook.buck.remoteexecution.proto.ExecutedActionInfo parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.facebook.buck.remoteexecution.proto.ExecutedActionInfo) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long cpuStatUsageUsec_ ;
    /**
     * <code>int64 cpu_stat_usage_usec = 1;</code>
     */
    public long getCpuStatUsageUsec() {
      return cpuStatUsageUsec_;
    }
    /**
     * <code>int64 cpu_stat_usage_usec = 1;</code>
     */
    public Builder setCpuStatUsageUsec(long value) {
      
      cpuStatUsageUsec_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 cpu_stat_usage_usec = 1;</code>
     */
    public Builder clearCpuStatUsageUsec() {
      
      cpuStatUsageUsec_ = 0L;
      onChanged();
      return this;
    }

    private long cpuStatUserUsec_ ;
    /**
     * <code>int64 cpu_stat_user_usec = 2;</code>
     */
    public long getCpuStatUserUsec() {
      return cpuStatUserUsec_;
    }
    /**
     * <code>int64 cpu_stat_user_usec = 2;</code>
     */
    public Builder setCpuStatUserUsec(long value) {
      
      cpuStatUserUsec_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 cpu_stat_user_usec = 2;</code>
     */
    public Builder clearCpuStatUserUsec() {
      
      cpuStatUserUsec_ = 0L;
      onChanged();
      return this;
    }

    private long cpuStatSystemUsec_ ;
    /**
     * <code>int64 cpu_stat_system_usec = 3;</code>
     */
    public long getCpuStatSystemUsec() {
      return cpuStatSystemUsec_;
    }
    /**
     * <code>int64 cpu_stat_system_usec = 3;</code>
     */
    public Builder setCpuStatSystemUsec(long value) {
      
      cpuStatSystemUsec_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 cpu_stat_system_usec = 3;</code>
     */
    public Builder clearCpuStatSystemUsec() {
      
      cpuStatSystemUsec_ = 0L;
      onChanged();
      return this;
    }

    private com.google.protobuf.BoolValue isFallbackEnabledForCompletedAction_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.BoolValue, com.google.protobuf.BoolValue.Builder, com.google.protobuf.BoolValueOrBuilder> isFallbackEnabledForCompletedActionBuilder_;
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public boolean hasIsFallbackEnabledForCompletedAction() {
      return isFallbackEnabledForCompletedActionBuilder_ != null || isFallbackEnabledForCompletedAction_ != null;
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public com.google.protobuf.BoolValue getIsFallbackEnabledForCompletedAction() {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        return isFallbackEnabledForCompletedAction_ == null ? com.google.protobuf.BoolValue.getDefaultInstance() : isFallbackEnabledForCompletedAction_;
      } else {
        return isFallbackEnabledForCompletedActionBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public Builder setIsFallbackEnabledForCompletedAction(com.google.protobuf.BoolValue value) {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        isFallbackEnabledForCompletedAction_ = value;
        onChanged();
      } else {
        isFallbackEnabledForCompletedActionBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public Builder setIsFallbackEnabledForCompletedAction(
        com.google.protobuf.BoolValue.Builder builderForValue) {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        isFallbackEnabledForCompletedAction_ = builderForValue.build();
        onChanged();
      } else {
        isFallbackEnabledForCompletedActionBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public Builder mergeIsFallbackEnabledForCompletedAction(com.google.protobuf.BoolValue value) {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        if (isFallbackEnabledForCompletedAction_ != null) {
          isFallbackEnabledForCompletedAction_ =
            com.google.protobuf.BoolValue.newBuilder(isFallbackEnabledForCompletedAction_).mergeFrom(value).buildPartial();
        } else {
          isFallbackEnabledForCompletedAction_ = value;
        }
        onChanged();
      } else {
        isFallbackEnabledForCompletedActionBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public Builder clearIsFallbackEnabledForCompletedAction() {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        isFallbackEnabledForCompletedAction_ = null;
        onChanged();
      } else {
        isFallbackEnabledForCompletedAction_ = null;
        isFallbackEnabledForCompletedActionBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public com.google.protobuf.BoolValue.Builder getIsFallbackEnabledForCompletedActionBuilder() {
      
      onChanged();
      return getIsFallbackEnabledForCompletedActionFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    public com.google.protobuf.BoolValueOrBuilder getIsFallbackEnabledForCompletedActionOrBuilder() {
      if (isFallbackEnabledForCompletedActionBuilder_ != null) {
        return isFallbackEnabledForCompletedActionBuilder_.getMessageOrBuilder();
      } else {
        return isFallbackEnabledForCompletedAction_ == null ?
            com.google.protobuf.BoolValue.getDefaultInstance() : isFallbackEnabledForCompletedAction_;
      }
    }
    /**
     * <pre>
     * Whether we should fallback to local retry if this action fails with exit code 1.
     * Fallback means we don't trust if this action failed and it may be flaky.
     * </pre>
     *
     * <code>.google.protobuf.BoolValue is_fallback_enabled_for_completed_action = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.BoolValue, com.google.protobuf.BoolValue.Builder, com.google.protobuf.BoolValueOrBuilder> 
        getIsFallbackEnabledForCompletedActionFieldBuilder() {
      if (isFallbackEnabledForCompletedActionBuilder_ == null) {
        isFallbackEnabledForCompletedActionBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.BoolValue, com.google.protobuf.BoolValue.Builder, com.google.protobuf.BoolValueOrBuilder>(
                getIsFallbackEnabledForCompletedAction(),
                getParentForChildren(),
                isClean());
        isFallbackEnabledForCompletedAction_ = null;
      }
      return isFallbackEnabledForCompletedActionBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:facebook.remote_execution.ExecutedActionInfo)
  }

  // @@protoc_insertion_point(class_scope:facebook.remote_execution.ExecutedActionInfo)
  private static final com.facebook.buck.remoteexecution.proto.ExecutedActionInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.facebook.buck.remoteexecution.proto.ExecutedActionInfo();
  }

  public static com.facebook.buck.remoteexecution.proto.ExecutedActionInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ExecutedActionInfo>
      PARSER = new com.google.protobuf.AbstractParser<ExecutedActionInfo>() {
    @java.lang.Override
    public ExecutedActionInfo parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ExecutedActionInfo(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ExecutedActionInfo> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ExecutedActionInfo> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.facebook.buck.remoteexecution.proto.ExecutedActionInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
